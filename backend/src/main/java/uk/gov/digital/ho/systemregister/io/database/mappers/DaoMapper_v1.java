package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SR_Sunset;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.PersonDAO_v1;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.RiskDAO_v1;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemAddedEventDAO_v1;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemDAO_v1;

import java.util.List;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.json.bind.Jsonb;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
@Named("v1")
public class DaoMapper_v1 implements DaoMapper<SystemAddedEventDAO_v1> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(SystemAddedEvent.class, SystemAddedEventDAO_v1.class);

    private final Jsonb jsonb;

    public DaoMapper_v1(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public SystemAddedEventDAO_v1 mapToDao(SR_Event evt) {
        if (!(evt instanceof SystemAddedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + evt.getClass().getName());
        }
        SystemAddedEvent systemAddedEvent = (SystemAddedEvent) evt;

        return new SystemAddedEventDAO_v1(
                toSystemDao(systemAddedEvent.system),
                systemAddedEvent.timestamp,
                toAuthorDao(systemAddedEvent.author));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends SR_Event> R mapToDomain(String data) {
        SystemAddedEventDAO_v1 event = jsonb.fromJson(data, SystemAddedEventDAO_v1.class);
        return (R) new SystemAddedEvent(fromSystemDao(event.system), fromAuthorDao(event.author), event.timestamp);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private SystemDAO_v1 toSystemDao(SR_System system) {
        List<RiskDAO_v1> risks = system.risks.stream()
                .map(risk -> new RiskDAO_v1(risk.name, risk.level, risk.rationale))
                .collect(toList());
        return new SystemDAO_v1(
                system.id,
                system.lastUpdated,
                system.name,
                system.description,
                system.portfolio,
                system.criticality,
                system.investmentState,
                system.businessOwner,
                system.serviceOwner,
                system.technicalOwner,
                system.productOwner,
                system.informationAssetOwner,
                system.developedBy,
                system.supportedBy,
                List.copyOf(system.aliases),
                risks);
    }

    private PersonDAO_v1 toAuthorDao(SR_Person author) {
        return new PersonDAO_v1(author.username);
    }

    private SR_System fromSystemDao(SystemDAO_v1 system) {
        List<SR_Risk> risks = system.risks.stream()
                .map(risk -> new SR_Risk(risk.name, risk.level, risk.rationale))
                .collect(toList());
        return new SR_System(system.id, system.name, system.description, system.lastUpdated, system.portfolio, system.criticality, system.investmentState, system.businessOwner,
                system.serviceOwner, system.technicalOwner, system.productOwner, system.informationAssetOwner, system.developedBy,
                system.supportedBy, List.copyOf(system.aliases), risks, new SR_Sunset(null, null));
    }

    private SR_Person fromAuthorDao(PersonDAO_v1 author) {
        return new SR_Person(author.name, null, null, null);
    }
}
