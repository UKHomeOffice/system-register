package uk.gov.digital.ho.systemregister.io.database;

import io.agroal.api.AgroalDataSource;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.util.AES;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import uk.gov.digital.ho.systemregister.util.EncryptionError;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        writeEvent(evt);
    }

    private Optional<Integer> writeSnapshot(Snapshot object) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_WRITE_SNAPSHOT);) {
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

    private <T extends SR_Event> Optional<Integer> writeEvent(T object) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_WRITE_EVENT);) {
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
        var encryptedData = AES.encrypt(json, encryptionKey);
        return encryptedData;
    }

    private <T> Optional<List<T>> readSnapshots(String sql) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            return transformResponse(resultSet);
        } catch (Exception e) {
            LOG.error("Massive database failure: ", e);
            return Optional.empty();
        }
    }

    private <T extends SR_Event> Optional<List<T>> readEvents(String sql, Instant timestamp) {
        try (Connection connection = dataSource.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setTimestamp(1, Timestamp.from(timestamp));
            ResultSet resultSet = preparedStatement.executeQuery();
            return transformResponse(resultSet);
        } catch (Exception e) {
            LOG.error("Massive database failure: ", e);
            return Optional.empty();
        }
    }

    private <T> Optional<List<T>> transformResponse(ResultSet resultSet) throws SQLException {
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            try {
                byte[] objectData = resultSet.getBytes("object_data");
                String objectType = resultSet.getString("object_type");
                String decrypted = AES.decrypt(objectData, encryptionKey);
                T deserialised = (T) jsonb.fromJson(decrypted, Class.forName(objectType));
                results.add(deserialised);
            } catch (Exception | EncryptionError e) {
                LOG.error("Error deserialising from db: " + e);
                return Optional.empty();
            }
        }
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results);
    }
}
