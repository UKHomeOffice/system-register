package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.PublicFacingUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.PublicFacingUpdatedEventDAO_v1;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Set;


@ApplicationScoped
public class PublicFacingUpdatedEventDaoMapper_v1 implements DaoMapper<PublicFacingUpdatedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(PublicFacingUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public PublicFacingUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public PublicFacingUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof PublicFacingUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        PublicFacingUpdatedEvent publicFacingUpdatedEvent = (PublicFacingUpdatedEvent) event;
        return new PublicFacingUpdatedEventDAO_v1(
                publicFacingUpdatedEvent.id,
                publicFacingUpdatedEvent.publicFacing,
                publicFacingUpdatedEvent.timestamp,
                toPersonDao(publicFacingUpdatedEvent.author)
                );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends SR_Event> R mapToDomain(String data) {
        PublicFacingUpdatedEventDAO_v1 event = jsonb.fromJson(data, PublicFacingUpdatedEventDAO_v1.class);
        return (R) new PublicFacingUpdatedEvent(event.id, event.publicFacing, fromPersonDao(event.author), event.timestamp);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private SR_Person fromPersonDao(PublicFacingUpdatedEventDAO_v1.Person person){
        return new SR_Person(person.username, person.firstName, person.surname, person.email);
    }

    private PublicFacingUpdatedEventDAO_v1.Person toPersonDao(SR_Person person){
        return new PublicFacingUpdatedEventDAO_v1.Person(person.username, person.firstName, person.surname, person.email);
    }
}
