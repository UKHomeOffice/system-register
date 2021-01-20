package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.InformationAssetOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.InformationAssetOwnerUpdatedEventDAO_v1;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Set;


@ApplicationScoped
public class InformationAssetOwnerUpdatedEventDaoMapper_v1 implements DaoMapper<InformationAssetOwnerUpdatedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(InformationAssetOwnerUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public InformationAssetOwnerUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public InformationAssetOwnerUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof InformationAssetOwnerUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        InformationAssetOwnerUpdatedEvent InformationAssetOwnerUpdatedEvent = (InformationAssetOwnerUpdatedEvent) event;
        return new InformationAssetOwnerUpdatedEventDAO_v1(
                InformationAssetOwnerUpdatedEvent.id,
                InformationAssetOwnerUpdatedEvent.informationAssetOwner,
                InformationAssetOwnerUpdatedEvent.timestamp,
                toPersonDao(InformationAssetOwnerUpdatedEvent.author)
                );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends SR_Event> R mapToDomain(String data) {
        InformationAssetOwnerUpdatedEventDAO_v1 event = jsonb.fromJson(data, InformationAssetOwnerUpdatedEventDAO_v1.class);
        return (R) new InformationAssetOwnerUpdatedEvent(event.id, event.informationAssetOwner, fromPersonDao(event.author), event.timestamp);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private SR_Person fromPersonDao(InformationAssetOwnerUpdatedEventDAO_v1.Person person){
        return new SR_Person(person.username, person.firstName, person.surname, person.email);
    }

    private InformationAssetOwnerUpdatedEventDAO_v1.Person toPersonDao(SR_Person person){
        return new InformationAssetOwnerUpdatedEventDAO_v1.Person(person.username, person.firstName, person.surname, person.email);
    }
}
