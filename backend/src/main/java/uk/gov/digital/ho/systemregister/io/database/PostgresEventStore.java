package uk.gov.digital.ho.systemregister.io.database;

import io.agroal.api.AgroalDataSource;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;
import uk.gov.digital.ho.systemregister.io.database.mappers.DaoMapper;
import uk.gov.digital.ho.systemregister.util.AES;
import uk.gov.digital.ho.systemregister.util.EncryptionError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

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
    @Named("v1")
    DaoMapper<? extends BaseDao> daoMapper;

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
    public void save(SR_Event evt) {
        var dao = daoMapper.mapToDao(evt);
        writeEvent(dao);
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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.from(timestamp));
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<List<EncodedData>> encodedData = transformResponse(resultSet);
            return encodedData.map(
                    data -> data.stream()
                            .map(this::<T>toDomainObject)
                            .collect(toList()));
        } catch (Exception e) {
            LOG.error("Massive database failure: ", e);
            return Optional.empty();
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
        return daoMapper;
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
