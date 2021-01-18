package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.PortfolioUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.PortfolioUpdatedEventDAO_v1;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Set;


@ApplicationScoped
public class PortfolioUpdatedEventDaoMapper_v1 implements DaoMapper<PortfolioUpdatedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(PortfolioUpdatedEventDAO_v1.class);

    private final Jsonb jsonb;

    public PortfolioUpdatedEventDaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public PortfolioUpdatedEventDAO_v1 mapToDao(SR_Event event) {
        if (!(event instanceof PortfolioUpdatedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + event.getClass().getName());
        }
        PortfolioUpdatedEvent investmentStateUpdatedEvent = (PortfolioUpdatedEvent) event;
        return new PortfolioUpdatedEventDAO_v1(
                investmentStateUpdatedEvent.id,
                investmentStateUpdatedEvent.portfolio,
                investmentStateUpdatedEvent.timestamp,
                toPersonDao(investmentStateUpdatedEvent.author)
                );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends SR_Event> R mapToDomain(String data) {
        PortfolioUpdatedEventDAO_v1 event = jsonb.fromJson(data, PortfolioUpdatedEventDAO_v1.class);
        return (R) new PortfolioUpdatedEvent(event.id, event.portfolio, fromPersonDao(event.author), event.timestamp);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private SR_Person fromPersonDao(PortfolioUpdatedEventDAO_v1.Person person){
        return new SR_Person(person.username, person.firstName, person.surname, person.email);
    }

    private PortfolioUpdatedEventDAO_v1.Person toPersonDao(SR_Person person){
        return new PortfolioUpdatedEventDAO_v1.Person(person.username, person.firstName, person.surname, person.email);
    }
}
