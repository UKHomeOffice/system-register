package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.InvestmentStateUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.ProductOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.InvestmentStateUpdatedEventDAO_v1;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.ProductOwnerUpdatedEventDAO_v1;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Set;


@ApplicationScoped
public class InvestmentStateUpdatedEventDaoMapper_v1 implements DaoMapper<InvestmentStateUpdatedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(InvestmentStateUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public InvestmentStateUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public InvestmentStateUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof InvestmentStateUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        InvestmentStateUpdatedEvent investmentStateUpdatedEvent = (InvestmentStateUpdatedEvent) event;
        return new InvestmentStateUpdatedEventDAO_v1(
                investmentStateUpdatedEvent.id,
                investmentStateUpdatedEvent.investmentState,
                investmentStateUpdatedEvent.timestamp,
                toPersonDao(investmentStateUpdatedEvent.author)
                );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends SR_Event> R mapToDomain(String data) {
        InvestmentStateUpdatedEventDAO_v1 event = jsonb.fromJson(data, InvestmentStateUpdatedEventDAO_v1.class);
        return (R) new InvestmentStateUpdatedEvent(event.id, event.investmentState, fromPersonDao(event.author), event.timestamp);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private SR_Person fromPersonDao(InvestmentStateUpdatedEventDAO_v1.Person person){
        return new SR_Person(person.username, person.firstName, person.surname, person.email);
    }

    private InvestmentStateUpdatedEventDAO_v1.Person toPersonDao(SR_Person person){
        return new InvestmentStateUpdatedEventDAO_v1.Person(person.username, person.firstName, person.surname, person.email);
    }
}
