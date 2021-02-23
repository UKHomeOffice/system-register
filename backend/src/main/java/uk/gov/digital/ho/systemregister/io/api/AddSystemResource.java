package uk.gov.digital.ho.systemregister.io.api;

import io.quarkus.security.Authenticated;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.AddSystemCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandProcessingException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.NoSuchSystemException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.api.dto.AddSystemCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.UpdatedSystemDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.time.Instant;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("api/systems")
public class AddSystemResource {
    private final AddSystemCommandHandler handler;
    private final AuthorMapper authorMapper;

    public AddSystemResource(AddSystemCommandHandler handler, AuthorMapper authorMapper) {
        this.handler = handler;
        this.authorMapper = authorMapper;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Authenticated
    public UpdatedSystemDTO addSystem(
            AddSystemCommandDTO dto,
            @Context SecurityContext securityContext
    ) throws CommandProcessingException, NoSuchSystemException {
        SR_Person author = authorMapper.fromSecurityContext(securityContext);
        AddSystemCommand command = dto.toCommand(author, Instant.now());

        var addedSystemAndMetadata = handler.handle(command);

        return UpdatedSystemDTO.from(addedSystemAndMetadata.getItem1(), addedSystemAndMetadata.getItem2());
    }
}
