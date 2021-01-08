package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemDescriptionUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemDescriptionUpdatedEventDAO_v1;

import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;

@ApplicationScoped
public class SystemDescriptionUpdatedEventDaoMapper_v1 implements DaoMapper<SystemDescriptionUpdatedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(SystemDescriptionUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public SystemDescriptionUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public SystemDescriptionUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof SystemDescriptionUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        SystemDescriptionUpdatedEvent systemDescriptionUpdatedEvent = (SystemDescriptionUpdatedEvent) event;
        return new SystemDescriptionUpdatedEventDAO_v1(
                systemDescriptionUpdatedEvent.id,
                systemDescriptionUpdatedEvent.description,
                systemDescriptionUpdatedEvent.timestamp,
                toPersonDao(systemDescriptionUpdatedEvent.author));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends SR_Event> R mapToDomain(String data) {
        SystemDescriptionUpdatedEventDAO_v1 dao = jsonb.fromJson(data, SystemDescriptionUpdatedEventDAO_v1.class);
        return (R) new SystemDescriptionUpdatedEvent(dao.id, dao.description, fromPersonDao(dao.author), dao.timestamp);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private SystemDescriptionUpdatedEventDAO_v1.Person toPersonDao(SR_Person author) {
        return new SystemDescriptionUpdatedEventDAO_v1.Person(
                author.username,
                author.firstName,
                author.surname,
                author.email);
    }

    private SR_Person fromPersonDao(SystemDescriptionUpdatedEventDAO_v1.Person author) {
        return new SR_Person(author.username, author.firstName, author.surname, author.email);
    }
}
