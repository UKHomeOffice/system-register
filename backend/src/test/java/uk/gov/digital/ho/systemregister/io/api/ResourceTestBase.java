package uk.gov.digital.ho.systemregister.io.api;

import io.agroal.api.AgroalDataSource;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ResourceTestBase {
    private final AgroalDataSource dataSource;

    public ResourceTestBase(@SuppressWarnings("CdiInjectionPointsInspection") AgroalDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @BeforeEach
    void cleanUpEventStore() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE eventstore.snapshots, eventstore.events;");
        }
    }
}
