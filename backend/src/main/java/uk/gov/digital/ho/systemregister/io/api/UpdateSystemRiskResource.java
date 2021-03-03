package uk.gov.digital.ho.systemregister.io.api;

import io.quarkus.security.Authenticated;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandProcessingException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.NoSuchSystemException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.UpdateSystemRiskCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemRiskCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.api.dto.UpdateSystemRiskCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.UpdatedSystemDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.time.Instant;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/systems")
public class UpdateSystemRiskResource {
    private final UpdateSystemRiskCommandHandler handler;
    private final AuthorMapper authorMapper;

    public UpdateSystemRiskResource(UpdateSystemRiskCommandHandler handler, AuthorMapper authorMapper) {
        this.handler = handler;
        this.authorMapper = authorMapper;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Authenticated
    @Path("/{systemId}/update-risk")
    public UpdatedSystemDTO updateSystemRisk(UpdateSystemRiskCommandDTO dto,
                                             @PathParam("systemId") int id,
                                             @Context SecurityContext securityContext)
            throws NoSuchSystemException, CommandProcessingException {
        SR_Person author = authorMapper.fromSecurityContext(securityContext);
        UpdateSystemRiskCommand command = dto.toCommand(id, author, Instant.now());

        var updatedSystemAndMetadata = handler.handle(command);

        return UpdatedSystemDTO.from(updatedSystemAndMetadata.getItem1(), updatedSystemAndMetadata.getItem2());
    }
}
