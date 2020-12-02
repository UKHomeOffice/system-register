package uk.gov.digital.ho.systemregister.test.io.database;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import uk.gov.digital.ho.systemregister.io.database.PostgresEventStore;
import uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "CI", matches = "drone")
public class PostgresEventStoreTest {
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
        assertThat(actual.get()).usingRecursiveComparison()
                .isEqualTo(List.of(expected));
    }

    @Test
    public void canReadV1SystemAddedEvent() {
        //insert pre-captured domain.SystemAddedEvent into db directly
        //eventstore.getEvents()
        //are all good and as expected
    }
}
