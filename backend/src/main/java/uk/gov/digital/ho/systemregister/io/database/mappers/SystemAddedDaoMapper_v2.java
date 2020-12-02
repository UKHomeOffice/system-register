package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemAddedEventDAO_v1;
import uk.gov.digital.ho.systemregister.io.database.dao.v2.SystemAddedEventDAO_v2;
import uk.gov.digital.ho.systemregister.io.database.dao.v2.SystemAddedEventDAO_v2.Person;
import uk.gov.digital.ho.systemregister.io.database.dao.v2.SystemAddedEventDAO_v2.Risk;
import uk.gov.digital.ho.systemregister.io.database.dao.v2.SystemAddedEventDAO_v2.System;

import java.util.List;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.json.bind.Jsonb;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
@Named("v2")
public class SystemAddedDaoMapper_v2 implements DaoMapper<SystemAddedEventDAO_v2> {
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(SystemAddedEventDAO_v2.class);

    private final Jsonb jsonb;

    public SystemAddedDaoMapper_v2(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public SystemAddedEventDAO_v2 mapToDao(SR_Event evt) {
        if (!(evt instanceof SystemAddedEvent)) {
            throw new UnsupportedOperationException(
                    "Event type is not supported: " + evt.getClass().getName());
        }
        SystemAddedEvent systemAddedEvent = (SystemAddedEvent) evt;

        return new SystemAddedEventDAO_v2(
                toSystemDao(systemAddedEvent.system),
                systemAddedEvent.timestamp,
                toAuthorDao(systemAddedEvent.author));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends SR_Event> R mapToDomain(String data) {
        SystemAddedEventDAO_v2 event = jsonb.fromJson(data, SystemAddedEventDAO_v2.class);
        return (R) new SystemAddedEvent(fromSystemDao(event.system), fromAuthorDao(event.author), event.timestamp);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private System toSystemDao(SR_System system) {
        List<Risk> risks = system.risks.stream()
                .map(risk -> new Risk(risk.name, risk.level, risk.rationale))
                .collect(toList());
        return new System(
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

    private Person toAuthorDao(SR_Person author) {
        return new Person(author.username, author.firstName, author.surname, author.email);
    }

    private SR_System fromSystemDao(System system) {
        List<SR_Risk> risks = system.risks.stream()
                .map(risk -> new SR_Risk(risk.name, risk.level, risk.rationale))
                .collect(toList());
        return new SR_System(
                system.id,
                system.name,
                system.description,
                system.lastUpdated,
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

    private SR_Person fromAuthorDao(Person author) {
        return new SR_Person(author.username, author.firstName, author.surname, author.email);
    }
}
