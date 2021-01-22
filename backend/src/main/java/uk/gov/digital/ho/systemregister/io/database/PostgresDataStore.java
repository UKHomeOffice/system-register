package uk.gov.digital.ho.systemregister.io.database;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PostgresDataStore implements DataStore {
    private static final String EVENT_INSERTION_SQL
            = "INSERT INTO eventstore.events (object_type, object_data, time_stamp)"
            + "  VALUES (?, ?, ?)";
    private static final String EVENT_QUERY_SQL
            = "SELECT object_data, object_type"
            + "  FROM eventstore.events"
            + "  WHERE time_stamp > ?";

    private final DataSource dataSource;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public PostgresDataStore(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(byte[] data, String typeId, Instant timestamp) throws DataStoreException {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(EVENT_INSERTION_SQL)
        ) {
            statement.setString(1, typeId);
            statement.setBytes(2, data);
            statement.setTimestamp(3, Timestamp.from(timestamp));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataStoreException("unable to save data", e);
        }
    }

    @Override
    public List<EncodedData> getData(Instant after) throws DataStoreException {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(EVENT_QUERY_SQL)
        ) {
            statement.setTimestamp(1, Timestamp.from(after));
            return fromResultSet(statement);
        } catch (SQLException e) {
            throw new DataStoreException("unable to retrieve data", e);
        }
    }

    private List<EncodedData> fromResultSet(PreparedStatement statement) throws SQLException {
        try (var resultSet = statement.executeQuery()) {
            var entries = new ArrayList<EncodedData>();
            while (resultSet.next()) {
                var data = resultSet.getBytes("object_data");
                var typeId = resultSet.getString("object_type");
                entries.add(new EncodedData(data, typeId));
            }
            return entries;
        }
    }
}
