package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemRisksUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemRisksUpdatedEventDAO_v1;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemRisksUpdatedEventDAO_v1.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Set;

@ApplicationScoped
public class SystemRisksUpdatedEventDaoMapper_v1 implements DaoMapper<SystemRisksUpdatedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(SystemRisksUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public SystemRisksUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public SystemRisksUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof SystemRisksUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        SystemRisksUpdatedEvent risksUpdatedEvent = (SystemRisksUpdatedEvent) event;
        return new SystemRisksUpdatedEventDAO_v1(
                risksUpdatedEvent.getSystemId(),
                risksUpdatedEvent.risks,
                risksUpdatedEvent.timestamp,
                toPersonDao(risksUpdatedEvent.author)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends SR_Event> R mapToDomain(String data) {
        SystemRisksUpdatedEventDAO_v1 event = jsonb.fromJson(data, SystemRisksUpdatedEventDAO_v1.class);
        return (R) new SystemRisksUpdatedEvent(
                event.id,
                event.risks,
                fromPersonDao(event.author),
                event.timestamp);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private Person toPersonDao(SR_Person author) {
        return new Person(author.username, author.firstName, author.surname, author.email);
    }

    private SR_Person fromPersonDao(Person author) {
        return new SR_Person(author.username, author.firstName, author.surname, author.email);
    }
}
