package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.BusinessOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.BusinessOwnerUpdatedEventDAO_v1;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Set;


@ApplicationScoped
public class BusinessOwnerUpdatedEventDaoMapper_v1 implements DaoMapper<BusinessOwnerUpdatedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(BusinessOwnerUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public BusinessOwnerUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public BusinessOwnerUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof BusinessOwnerUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        BusinessOwnerUpdatedEvent businessOwnerUpdatedEvent = (BusinessOwnerUpdatedEvent) event;
        return new BusinessOwnerUpdatedEventDAO_v1(
                businessOwnerUpdatedEvent.id,
                businessOwnerUpdatedEvent.businessOwner,
                businessOwnerUpdatedEvent.timestamp,
                toPersonDao(businessOwnerUpdatedEvent.author)
                );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends SR_Event> R mapToDomain(String data) {
        BusinessOwnerUpdatedEventDAO_v1 event = jsonb.fromJson(data, BusinessOwnerUpdatedEventDAO_v1.class);
        return (R) new BusinessOwnerUpdatedEvent(event.id, event.businessOwner, fromPersonDao(event.author), event.timestamp);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private SR_Person fromPersonDao(BusinessOwnerUpdatedEventDAO_v1.Person person){
        return new SR_Person(person.username, person.firstName, person.surname, person.email);
    }

    private BusinessOwnerUpdatedEventDAO_v1.Person toPersonDao(SR_Person person){
        return new BusinessOwnerUpdatedEventDAO_v1.Person(person.username, person.firstName, person.surname, person.email);
    }
}
