package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemNameCommand;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

@ApplicationScoped
public class UpdateSystemNameCommandHandler {
    public Tuple2<SR_System, UpdateMetadata> handle(@Valid UpdateSystemNameCommand command) {
        return Tuple2.of(null, null);
    }
}