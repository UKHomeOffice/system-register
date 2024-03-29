package uk.gov.digital.ho.systemregister.io.database;

import io.agroal.api.AgroalDataSource;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.events.*;
import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;
import uk.gov.digital.ho.systemregister.io.database.mappers.*;
import uk.gov.digital.ho.systemregister.util.AES;
import uk.gov.digital.ho.systemregister.util.EncryptionError;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
@Named("postgres")
public class PostgresEventStore implements IEventStore {
    private static final String SQL_READ_LATEST_SNAPSHOT =
            "SELECT object_data, object_type FROM eventstore.snapshots ORDER BY id DESC LIMIT 1";
    private static final String SQL_WRITE_SNAPSHOT =
            "INSERT INTO eventstore.snapshots(object_type, object_data) VALUES (?, ?)";
    private static final String SQL_WRITE_EVENT =
            "INSERT INTO eventstore.events(time_stamp, object_type, object_data) VALUES (?, ?, ?)";
    private static final String SQL_READ_EVENTS =
            "SELECT object_data, object_type FROM eventstore.events WHERE time_stamp > ?";

    private static final Logger LOG = Logger.getLogger(PostgresEventStore.class);

    @ConfigProperty(name = "database.encryptionKey")
    String encryptionKey;

    @Inject
    AgroalDataSource dataSource;

    @Inject
    SystemAddedDaoMapper_v2 systemAddedDaoMapper;

    @Inject
    SystemNameUpdatedEventDaoMapper_v1 systemNameUpdatedDaoMapper;

    @Inject
    SystemDescriptionUpdatedEventDaoMapper_v1 systemDescriptionUpdatedDaoMapper;

    @Inject
    SystemAliasesUpdatedEventDaoMapper_v1 aliasesUpdatedDaoMapper;

    @Inject
    CriticalityUpdatedEventDaoMapper_v1 criticalityUpdatedDaoMapper;

    @Inject
    InvestmentStateUpdatedEventDaoMapper_v1 investmentStateUpdatedDaoMapper;

    @Inject
    PortfolioUpdatedEventDaoMapper_v1 portfolioUpdatedDaoMapper;

    @Inject
    ProductOwnerUpdatedEventDaoMapper_v1 productOwnerUpdatedDaoMapper;

    @Inject
    InformationAssetOwnerUpdatedEventDaoMapper_v1 informationAssetOwnerUpdatedDaoMapper;

    @Inject
    TechnicalOwnerUpdatedEventDaoMapper_v1 technicalOwnerUpdatedDaoMapper;

    @Inject
    BusinessOwnerUpdatedEventDaoMapper_v1 businessOwnerUpdatedDaoMapper;

    @Inject
    ServiceOwnerUpdatedEventDaoMapper_v1 serviceOwnerUpdatedDaoMapper;

    @Inject
    DevelopedByUpdatedEventDaoMapper_v1 developedByUpdatedDaoMapper;

    @Inject
    SupportedByUpdatedEventDaoMapper_v1 supportedByUpdatedDaoMapper;

    @Inject
    SystemRiskUpdatedEventDaoMapper_v1 risksUpdatedDaoMapper;

    @Inject
    SunsetUpdatedEventDaoMapper_v1 sunsetUpdatedDaoMapper;

    @Inject
    PublicFacingUpdatedEventDaoMapper_v1 publicFacingDaoMapper;


    @Inject
    Instance<DaoMapper<? extends BaseDao>> mappers;

    Jsonb jsonb = JsonbBuilder.create();

    @Override
    public Optional<Snapshot> getSnapshot() {
        Optional<List<Snapshot>> stuff = readSnapshots(SQL_READ_LATEST_SNAPSHOT);
        if (stuff.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(stuff.get().get(0));
    }

    @Override
    public Optional<List<SR_Event>> getEvents() {
        return getEvents(Instant.EPOCH);
    }

    @Override
    public Optional<List<SR_Event>> getEvents(Instant from) {
        Optional<List<SR_Event>> stuff = readEvents(SQL_READ_EVENTS, from);
        if (stuff.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(stuff.get());
    }

    @Override
    public void save(Snapshot snapshot) {
        writeSnapshot(snapshot);
    }

    @Override
    public void save(SR_Event event) {
        DaoMapper<? extends BaseDao> daoMapper = findDaoMapperFor(event);

        var dao = daoMapper.mapToDao(event);

        writeEvent(dao);
    }

    private DaoMapper<? extends BaseDao> findDaoMapperFor(SR_Event event) {
        DaoMapper<? extends BaseDao> daoMapper;

        if (event instanceof SystemAddedEvent) {
            daoMapper = systemAddedDaoMapper;
        } else if (event instanceof SystemNameUpdatedEvent) {
            daoMapper = systemNameUpdatedDaoMapper;
        } else if (event instanceof SystemDescriptionUpdatedEvent) {
            daoMapper = systemDescriptionUpdatedDaoMapper;
        } else if (event instanceof SystemAliasesUpdatedEvent) {
            daoMapper = aliasesUpdatedDaoMapper;
        } else if (event instanceof CriticalityUpdatedEvent) {
            daoMapper = criticalityUpdatedDaoMapper;
        } else if (event instanceof InvestmentStateUpdatedEvent) {
            daoMapper = investmentStateUpdatedDaoMapper;
        } else if (event instanceof PortfolioUpdatedEvent) {
            daoMapper = portfolioUpdatedDaoMapper;
        } else if (event instanceof ProductOwnerUpdatedEvent) {
            daoMapper = productOwnerUpdatedDaoMapper;
        } else if (event instanceof InformationAssetOwnerUpdatedEvent) {
            daoMapper = informationAssetOwnerUpdatedDaoMapper;
        } else if (event instanceof TechnicalOwnerUpdatedEvent) {
            daoMapper = technicalOwnerUpdatedDaoMapper;
        } else if (event instanceof ServiceOwnerUpdatedEvent) {
            daoMapper = serviceOwnerUpdatedDaoMapper;
        } else if (event instanceof BusinessOwnerUpdatedEvent) {
            daoMapper = businessOwnerUpdatedDaoMapper;
        } else if (event instanceof DevelopedByUpdatedEvent) {
            daoMapper = developedByUpdatedDaoMapper;
        } else if (event instanceof SupportedByUpdatedEvent) {
            daoMapper = supportedByUpdatedDaoMapper;
        } else if (event instanceof SystemRiskUpdatedEvent) {
            daoMapper = risksUpdatedDaoMapper;
        } else if (event instanceof SunsetUpdatedEvent) {
            daoMapper = sunsetUpdatedDaoMapper;
        } else if (event instanceof PublicFacingUpdatedEvent) {
            daoMapper = publicFacingDaoMapper;
        } else {
            throw new UnsupportedOperationException("Event type not supported: " + event.getClass().getName() + ". Please implement a DAO Mapper for this event type");
        }
        return daoMapper;
    }

    private Optional<Integer> writeSnapshot(Snapshot object) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_WRITE_SNAPSHOT)) {
            String className = object.getClass().getName();
            byte[] encryptedData = serialise(object);
            preparedStatement.setString(1, className);
            preparedStatement.setObject(2, encryptedData);
            var cnt = preparedStatement.executeUpdate();
            return Optional.of(cnt);
        } catch (Exception | EncryptionError e) {
            LOG.error("Error persisting snapshot: ", e);
            return Optional.empty();
        }
    }

    private <T extends BaseDao> Optional<Integer> writeEvent(T object) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_WRITE_EVENT)) {
            String className = object.getClass().getName();
            byte[] encryptedData = serialise(object);
            preparedStatement.setTimestamp(1, Timestamp.from(object.timestamp));
            preparedStatement.setString(2, className);
            preparedStatement.setObject(3, encryptedData);
            var cnt = preparedStatement.executeUpdate();

            return Optional.of(cnt);
        } catch (Exception | EncryptionError e) {
            LOG.error("Error persisting event: ", e);
            return Optional.empty();
        }
    }

    private byte[] serialise(Object object) throws EncryptionError {
        var json = jsonb.toJson(object);
        return AES.encrypt(json, encryptionKey);
    }

    private <T> Optional<List<T>> readSnapshots(String sql) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            Optional<List<EncodedData>> encodedData = transformResponse(resultSet);
            return encodedData.map(
                    data -> data.stream()
                            .map(datum -> (T) jsonb.fromJson(datum.data, datum.type))
                            .collect(toList()));
        } catch (Exception e) {
            LOG.error("Massive database failure: ", e);
            return Optional.empty();
        }
    }

    private <T extends SR_Event> Optional<List<T>> readEvents(String sql, Instant timestamp) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setTimestamp(1, Timestamp.from(timestamp));
            Optional<List<EncodedData>> encodedData = readEncodedEvents(preparedStatement);
            return encodedData.map(
                    data -> data.stream()
                            .map(this::<T>toDomainObject)
                            .collect(toList()));
        } catch (Exception e) {
            LOG.error("Massive database failure: ", e);
            return Optional.empty();
        }
    }

    private Optional<List<EncodedData>> readEncodedEvents(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            return transformResponse(resultSet);
        }
    }

    private Optional<List<EncodedData>> transformResponse(ResultSet resultSet) throws SQLException {
        List<EncodedData> results = new ArrayList<>();
        while (resultSet.next()) {
            try {
                byte[] objectData = resultSet.getBytes("object_data");
                String objectType = resultSet.getString("object_type");
                String decrypted = AES.decrypt(objectData, encryptionKey);
                results.add(EncodedData.from(Class.forName(objectType), decrypted));
            } catch (Exception | EncryptionError e) {
                LOG.error("Error deserialising from db: " + e);
                return Optional.empty();
            }
        }
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results);
    }

    private <T extends SR_Event> T toDomainObject(EncodedData datum) {
        var mapper = findMapperFor(datum.type);
        return mapper.mapToDomain(datum.data);
    }

    private DaoMapper<? extends BaseDao> findMapperFor(Class<?> type) {
        return mappers.stream()
                .filter(mapper -> mapper.supports(type))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("no mapper for type: " + type.getName()));
    }

    @SuppressWarnings("CdiInjectionPointsInspection")
    private static class EncodedData {
        final Class<?> type;
        final String data;

        EncodedData(Class<?> type, String data) {
            this.type = type;
            this.data = data;
        }

        private static EncodedData from(Class<?> type, String data) {
            return new EncodedData(type, data);
        }
    }
}
