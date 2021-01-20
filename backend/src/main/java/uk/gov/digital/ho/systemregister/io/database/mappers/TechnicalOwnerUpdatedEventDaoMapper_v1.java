package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.TechnicalOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.TechnicalOwnerUpdatedEventDAO_v1;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.TechnicalOwnerUpdatedEventDAO_v1.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Set;

@ApplicationScoped
public class TechnicalOwnerUpdatedEventDaoMapper_v1 implements DaoMapper<TechnicalOwnerUpdatedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(TechnicalOwnerUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public TechnicalOwnerUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public TechnicalOwnerUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof TechnicalOwnerUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        TechnicalOwnerUpdatedEvent technicalOwnerUpdatedEvent = (TechnicalOwnerUpdatedEvent) event;
        return new TechnicalOwnerUpdatedEventDAO_v1(
                technicalOwnerUpdatedEvent.getSystemId(),
                technicalOwnerUpdatedEvent.technicalOwner,
                technicalOwnerUpdatedEvent.timestamp,
                toPersonDao(technicalOwnerUpdatedEvent.author)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends SR_Event> R mapToDomain(String data) {
        TechnicalOwnerUpdatedEventDAO_v1 event = jsonb.fromJson(data, TechnicalOwnerUpdatedEventDAO_v1.class);
        return (R) new TechnicalOwnerUpdatedEvent(
                event.id,
                event.technicalOwner,
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
