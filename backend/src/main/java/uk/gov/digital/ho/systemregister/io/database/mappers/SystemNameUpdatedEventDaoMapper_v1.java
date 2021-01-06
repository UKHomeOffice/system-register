package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SystemNameUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemNameUpdatedEventDAO_v1;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Set;

@ApplicationScoped
public class SystemNameUpdatedEventDaoMapper_v1 implements DaoMapper<SystemNameUpdatedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(SystemNameUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public SystemNameUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }


    @Override
    public SystemNameUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof SystemNameUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        SystemNameUpdatedEvent systemNameUpdatedEvent = (SystemNameUpdatedEvent) event;
        return new SystemNameUpdatedEventDAO_v1(
                systemNameUpdatedEvent.id,
                systemNameUpdatedEvent.name,
                systemNameUpdatedEvent.timestamp,
                toPersonDao(systemNameUpdatedEvent.author)
        );

    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    public <R extends SR_Event> R mapToDomain(String data) {
        SystemNameUpdatedEventDAO_v1 event = jsonb.fromJson(data, SystemNameUpdatedEventDAO_v1.class);
        return (R) new SystemNameUpdatedEvent(event.id, event.name, fromPersonDao(event.author), event.timestamp);
    }

    private SystemNameUpdatedEventDAO_v1.Person toPersonDao(SR_Person person) {
        return new SystemNameUpdatedEventDAO_v1.Person(person.username, person.firstName, person.surname, person.email);
    }

    private SR_Person fromPersonDao(SystemNameUpdatedEventDAO_v1.Person person){
        return new SR_Person(person.username, person.firstName, person.surname, person.email);
    }
}
