package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.CriticalityUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.CriticalityUpdatedEventDAO_v1;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Set;

@ApplicationScoped
public class CriticalityUpdatedEventDaoMapper_v1 implements DaoMapper<CriticalityUpdatedEventDAO_v1>{
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(CriticalityUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public CriticalityUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public CriticalityUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof CriticalityUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        CriticalityUpdatedEvent criticalityUpdatedEvent = (CriticalityUpdatedEvent) event;
        return new CriticalityUpdatedEventDAO_v1(
                criticalityUpdatedEvent.id,
                criticalityUpdatedEvent.criticality,
                criticalityUpdatedEvent.timestamp,
                toPersonDao(criticalityUpdatedEvent.author)
        );
    }

    @Override
    public <R extends SR_Event> R mapToDomain(String data) {
        CriticalityUpdatedEventDAO_v1 event = jsonb.fromJson(data, CriticalityUpdatedEventDAO_v1.class);
        return (R) new CriticalityUpdatedEvent(fromPersonDao(event.author), event.timestamp, event.id, event.criticality);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }


    private SR_Person fromPersonDao(CriticalityUpdatedEventDAO_v1.Person person){
        return new SR_Person(person.username, person.firstName, person.surname, person.email);
    }

    private CriticalityUpdatedEventDAO_v1.Person toPersonDao(SR_Person person){
        return new CriticalityUpdatedEventDAO_v1.Person(person.username, person.firstName, person.surname, person.email);
    }
}
