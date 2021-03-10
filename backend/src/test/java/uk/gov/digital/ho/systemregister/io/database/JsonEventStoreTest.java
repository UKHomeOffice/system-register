package uk.gov.digital.ho.systemregister.io.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;

import javax.enterprise.inject.Instance;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void raisesExceptionIfEventCannotBeEncoded() {
        when(codec.canEncode(any()))
                .thenReturn(false);

        assertThatThrownBy(() -> eventStore.save(new TestEvent(null)))
                .isInstanceOf(EventStoreException.class)
                .hasMessageContaining("event not supported");
    }

    @Test
    void raisesExceptionIfEventCannotBeDecoded() throws DataStoreException {
        when(dataStore.getData(EPOCH))
                .thenReturn(List.of(new DataStore.EncodedData(new byte[0], null)));
        when(codec.canDecode(any()))
                .thenReturn(false);

        assertThatThrownBy(() -> eventStore.getEvents())
                .isInstanceOf(EventStoreException.class)
                .hasMessageContaining("event not supported");
    }

    @Nested
    class CodecSelection {
        private final Codec oldCodec = supportedCodec();
        private final Codec newCodec = supportedCodec();
        private final Codec unsupportedCodec = unsupportedCodec();

        private JsonEventStore eventStore;

        private Codec supportedCodec() {
            var codec = codec(true);
            when(codec.encode(any())).thenReturn(new Codec.EncodedData(new byte[0], null));
            return codec;
        }

        private Codec unsupportedCodec() {
            return codec(false);
        }

        private Codec codec(boolean supported) {
            var codec = mock(Codec.class);
            when(codec.canEncode(any())).thenReturn(supported);
            when(codec.canDecode(any())).thenReturn(supported);
            return codec;
        }

        @BeforeEach
        void setUp() throws DataStoreException {
            when(unsupportedCodec.compareTo(any())).thenReturn(1);
            when(newCodec.compareTo(unsupportedCodec)).thenReturn(-1);
            when(newCodec.compareTo(oldCodec)).thenReturn(1);
            when(oldCodec.compareTo(any())).thenReturn(-1);

            when(codecs.stream())
                    .thenReturn(Stream.of(unsupportedCodec, oldCodec, newCodec));
            when(dataStore.getData(any()))
                    .thenReturn(List.of(new DataStore.EncodedData(new byte[0], null)));

            eventStore = new JsonEventStore(dataStore, codecs);
        }

        @Test
        void selectsTheCodecWithTheHighestVersionWhenSaving() throws EventStoreException {
            eventStore.save(new TestEvent(null));

            verify(oldCodec, never()).encode(any());
            verify(newCodec).encode(any());
        }

        @Test
        void ignoresUnsupportedCodecsWhenSaving() throws EventStoreException {
            eventStore.save(new TestEvent(null));

            verify(unsupportedCodec, never()).encode(any());
            verify(newCodec).encode(any());
        }

        @Test
        void selectsTheCodecWithTheHighestVersionWhenRetrieving() throws EventStoreException {
            eventStore.getEvents();

            verify(oldCodec, never()).decode(any(), any());
            verify(newCodec).decode(any(), any());
        }

        @Test
        void ignoresUnsupportedCodecsWhenRetrieving() throws EventStoreException {
            eventStore.getEvents();

            verify(unsupportedCodec, never()).decode(any(), any());
            verify(newCodec).decode(any(), any());
        }
    }

    @SuppressWarnings("CdiInjectionPointsInspection")
    public static class TestEvent extends SR_Event {
        TestEvent(Instant timestamp) {
            super(null, timestamp);
        }
    }
}
