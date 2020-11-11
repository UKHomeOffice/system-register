package uk.gov.digital.ho.systemregister.test.io.database;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import uk.gov.digital.ho.systemregister.io.database.PostgresEventStore;
import uk.gov.digital.ho.systemregister.test.helpers.KeycloakServer;
import uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "CI", matches = "drone")
@QuarkusTestResource(KeycloakServer.class)
public class PostgresEventStoreTest {
    private static final Logger LOG = Logger.getLogger(PostgresEventStoreTest.class);

    @Inject
    PostgresEventStore eventStore;

    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    AgroalDataSource dataSource;

    @BeforeEach
    void cleanUpEventStore() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE eventstore.snapshots, eventstore.events;");
        }
    }

    @Test
    public void getSnapshot_Empty() {
        var actual = eventStore.getSnapshot();

        assertTrue(actual.isEmpty());
    }

    @Test
    public void saveSnapshot() {
        var expected = new SnapshotBuilder().withASystem().build();
        eventStore.save(expected);

        var actual = eventStore.getSnapshot();

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void saveLargeSnapshot() {
        var expected = new SnapshotBuilder().withManySystems(5000).build();
        eventStore.save(expected);

        var actual = eventStore.getSnapshot();

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void saveSystemAddedEvent() {
        var expected = new SystemAddedEventBuilder().build();
        eventStore.save(expected);

        var actual = eventStore.getEvents();

        assertTrue(actual.isPresent());
        assertEquals(1, actual.get().size());
        assertEquals(expected, actual.get().get(0));
    }
}
