package uk.gov.digital.ho.systemregister.io.database;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import javax.inject.Inject;
import java.sql.*;
import java.time.Instant;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "CI", matches = "drone")
class PostgresDataStoreTest {
    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    AgroalDataSource dataSource;

    private PostgresDataStore dataStore;

    @BeforeEach
    void setUp() {
        dataStore = new PostgresDataStore(dataSource);
    }

    @BeforeEach
    void cleanUpEventStore() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.execute("TRUNCATE eventstore.snapshots, eventstore.events;");
        }
    }

    @Test
    void writesDataToDatabase() throws SQLException, DataStoreException {
        var expectedTimestamp = Instant.now();
        var expectedData = "some data".getBytes(UTF_8);

        dataStore.save(expectedData, "type-id", expectedTimestamp);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM eventstore.events;");
             ResultSet resultSet = statement.executeQuery()
        ) {
            assertThat(resultSet.next()).isTrue();

            var data = resultSet.getBytes("object_data");
            var typeId = resultSet.getString("object_type");
            var timestamp = resultSet.getTimestamp("time_stamp").toInstant();
            assertThat(data).containsExactly(expectedData);
            assertThat(typeId).isEqualTo("type-id");
            assertThat(timestamp).isEqualTo(expectedTimestamp);
        }
    }

    @Test
    void readsDataFromDatabase() throws DataStoreException {
        var timestamp = Instant.now();
        var data = "data content".getBytes(UTF_8);
        var moreData = "additional data content".getBytes(UTF_8);
        insertEvent(data, "a-type-id", timestamp);
        insertEvent(moreData, "another-type-id", timestamp);

        var entries = dataStore.getData(EPOCH);

        assertThat(entries).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        new DataStore.EncodedData(data, "a-type-id"),
                        new DataStore.EncodedData(moreData, "another-type-id"));
    }

    @Test
    void onlyReturnsDataMoreRecentAfterTheGivenTimestamp() throws DataStoreException {
        var timestamp = Instant.now();
        var earlierTimestamp = timestamp.minusMillis(1);
        var laterTimestamp = timestamp.plusMillis(1);
        var typeId = "type-id";
        insertEvent(new byte[] {0}, typeId, earlierTimestamp);
        insertEvent(new byte[] {1}, typeId, laterTimestamp);
        insertEvent(new byte[] {2}, typeId, timestamp);

        var entries = dataStore.getData(timestamp);

        assertThat(entries).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(new DataStore.EncodedData(new byte[] {1}, typeId));
    }

    private void insertEvent(byte[] data, @SuppressWarnings("SameParameterValue") String typeId, Instant timestamp) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO eventstore.events (time_stamp, object_type, object_data) VALUES (?, ?, ?);");
        ) {
            statement.setTimestamp(1, Timestamp.from(timestamp));
            statement.setString(2, typeId);
            statement.setBytes(3, data);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new AssertionError("unable to insert event", e);
        }
    }
}
