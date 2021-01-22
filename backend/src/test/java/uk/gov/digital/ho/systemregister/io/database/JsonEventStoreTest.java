package uk.gov.digital.ho.systemregister.io.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;

import javax.enterprise.inject.Instance;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JsonEventStoreTest {
    private final DataStore dataStore = mock(DataStore.class);
    private final Codec codec = mock(Codec.class);

    @SuppressWarnings("unchecked")
    private final Instance<Codec> codecs = mock(Instance.class);

    private JsonEventStore eventStore;

    @BeforeEach
    void setUp() {
        eventStore = new JsonEventStore(dataStore, codecs);

        when(codecs.stream()).thenReturn(Stream.of(codec));
        when(codec.canEncode(any())).thenReturn(true);
        when(codec.canDecode(any())).thenReturn(true);
    }

    @Test
    void sendsAnEncodedEventToDataStore() throws EventStoreException, DataStoreException {
        var encodedData = "{}".getBytes(UTF_8);
        var timestamp = Instant.parse("1970-01-02T03:04:05Z");
        var event = new TestEvent(timestamp);
        when(codec.encode(event))
                .thenReturn(new Codec.EncodedData(encodedData, "type-id"));

        eventStore.save(event);

        verify(dataStore).save(encodedData, "type-id", event.timestamp);
    }

    @Test
    void decodesEventsFromDataStore() throws EventStoreException, DataStoreException {
        var event = new TestEvent(Instant.now());
        var typeId = "another-type-id";
        var encodedData = "{}".getBytes(UTF_8);
        var entries = List.of(new DataStore.EncodedData(encodedData, typeId));
        when(codec.decode(encodedData, typeId))
                .thenReturn(event);
        when(dataStore.getData(EPOCH))
                .thenReturn(entries);

        List<SR_Event> events = eventStore.getEvents();

        assertThat(events).containsExactly(event);
    }

    @SuppressWarnings("CdiInjectionPointsInspection")
    public static class TestEvent extends SR_Event {
        TestEvent(Instant timestamp) {
            super(null, timestamp);
        }
    }
}
