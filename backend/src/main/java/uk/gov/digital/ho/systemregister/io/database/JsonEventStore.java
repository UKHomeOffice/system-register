package uk.gov.digital.ho.systemregister.io.database;

import edu.umd.cs.findbugs.annotations.NonNull;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;

import javax.enterprise.inject.Instance;
import java.time.Instant;
import java.util.List;

import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;

public class JsonEventStore implements EventStore {
    private final DataStore dataStore;
    private final Instance<Codec> codecs;

    public JsonEventStore(DataStore dataStore, Instance<Codec> codecs) {
        this.dataStore = dataStore;
        this.codecs = codecs;
    }

    @Override
    public void save(@NonNull SR_Event event) throws EventStoreException {
        Codec codec = codecs.stream()
                .filter(theCodec -> theCodec.canEncode(event))
                .max(naturalOrder())
                .orElseThrow(() -> new EventStoreException("event not supported: " + event.getClass().getSimpleName()));

        var encodedData = codec.encode(event);

        try {
            dataStore.save(encodedData.getData(), encodedData.getTypeId(), event.timestamp);
        } catch (DataStoreException e) {
            throw new EventStoreException("unable to save event", e);
        }
    }

    @Override
    public List<SR_Event> getEvents(@NonNull Instant after) throws EventStoreException {
        try {
            return dataStore.getData(after).stream()
                    .map(this::toEvent)
                    .collect(toList());
        } catch (UnsupportedTypeIdException e) {
            throw new EventStoreException("event not supported: " + e.typeId);
        } catch (DataStoreException e) {
            throw new EventStoreException("unable to retrieve events", e);
        }
    }

    private SR_Event toEvent(DataStore.EncodedData encodedData) {
        Codec codec = codecs.stream()
                .filter(theCodec -> theCodec.canDecode(encodedData.getTypeId()))
                .max(naturalOrder())
                .orElseThrow(() -> new UnsupportedTypeIdException(encodedData.getTypeId()));

        return codec.decode(encodedData.getData(), encodedData.getTypeId());
    }

    @SuppressWarnings({"QsPrivateBeanMembersInspection", "CdiInjectionPointsInspection"})
    private static class UnsupportedTypeIdException extends RuntimeException {
        private final String typeId;

        private UnsupportedTypeIdException(String typeId) {
            this.typeId = typeId;
        }
    }
}
