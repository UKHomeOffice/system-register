package uk.gov.digital.ho.systemregister.test.io.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import io.quarkus.test.junit.QuarkusTest;
import uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder;
import uk.gov.digital.ho.systemregister.io.database.PostgresEventStore;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "CI", matches = "drone")
public class PostgresEventStoreTest {
    private static final Logger LOG = Logger.getLogger(PostgresEventStoreTest.class);

    @Inject
    PostgresEventStore eventStore;

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
