package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.PersonDAO_v1;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.RiskDAO_v1;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemAddedEventDAO_v1;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemDAO_v1;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
@Named("v1")
public class DaoMapper_v1 implements DaoMapper<SystemAddedEventDAO_v1> {
    @Override
    public SystemAddedEventDAO_v1 map(SR_Event evt) {
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
        return new PersonDAO_v1(author.name);
    }
}
