package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.DevelopedByUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.DevelopedByUpdatedEventDAO_v1;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Set;


@ApplicationScoped
public class DevelopedByUpdatedEventDaoMapper_v1 implements DaoMapper<DevelopedByUpdatedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(DevelopedByUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public DevelopedByUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public DevelopedByUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof DevelopedByUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        DevelopedByUpdatedEvent developedByUpdatedEvent = (DevelopedByUpdatedEvent) event;
        return new DevelopedByUpdatedEventDAO_v1(
                developedByUpdatedEvent.id,
                developedByUpdatedEvent.developedBy,
                developedByUpdatedEvent.timestamp,
                toPersonDao(developedByUpdatedEvent.author)
                );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends SR_Event> R mapToDomain(String data) {
        DevelopedByUpdatedEventDAO_v1 event = jsonb.fromJson(data, DevelopedByUpdatedEventDAO_v1.class);
        return (R) new DevelopedByUpdatedEvent(event.id, event.developedBy, fromPersonDao(event.author), event.timestamp);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private SR_Person fromPersonDao(DevelopedByUpdatedEventDAO_v1.Person person){
        return new SR_Person(person.username, person.firstName, person.surname, person.email);
    }

    private DevelopedByUpdatedEventDAO_v1.Person toPersonDao(SR_Person person){
        return new DevelopedByUpdatedEventDAO_v1.Person(person.username, person.firstName, person.surname, person.email);
    }
}
