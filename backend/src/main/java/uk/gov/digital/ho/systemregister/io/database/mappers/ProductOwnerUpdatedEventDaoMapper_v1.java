package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.ProductOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.ProductOwnerUpdatedEventDAO_v1;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Set;


@ApplicationScoped
public class ProductOwnerUpdatedEventDaoMapper_v1 implements DaoMapper<ProductOwnerUpdatedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(ProductOwnerUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public ProductOwnerUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public ProductOwnerUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof ProductOwnerUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        ProductOwnerUpdatedEvent productOwnerUpdatedEvent = (ProductOwnerUpdatedEvent) event;
        return new ProductOwnerUpdatedEventDAO_v1(
                productOwnerUpdatedEvent.id,
                productOwnerUpdatedEvent.productOwner,
                productOwnerUpdatedEvent.timestamp,
                toPersonDao(productOwnerUpdatedEvent.author)
                );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends SR_Event> R mapToDomain(String data) {
        ProductOwnerUpdatedEventDAO_v1 event = jsonb.fromJson(data, ProductOwnerUpdatedEventDAO_v1.class);
        return (R) new ProductOwnerUpdatedEvent(event.id, event.productOwner, fromPersonDao(event.author), event.timestamp);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private SR_Person fromPersonDao(ProductOwnerUpdatedEventDAO_v1.Person person){
        return new SR_Person(person.username, person.firstName, person.surname, person.email);
    }

    private ProductOwnerUpdatedEventDAO_v1.Person toPersonDao(SR_Person person){
        return new ProductOwnerUpdatedEventDAO_v1.Person(person.username, person.firstName, person.surname, person.email);
    }
}
