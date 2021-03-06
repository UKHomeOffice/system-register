package uk.gov.digital.ho.systemregister.io.api;

import io.quarkus.security.Authenticated;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandProcessingException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.NoSuchSystemException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.UpdateInvestmentStateCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateInvestmentStateCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.api.dto.UpdateInvestmentStateCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.UpdatedSystemDTO;

import java.time.Instant;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/systems")
public class UpdateInvestmentStateResource {
    private final UpdateInvestmentStateCommandHandler handler;
    private final AuthorMapper authorMapper;

    public UpdateInvestmentStateResource(UpdateInvestmentStateCommandHandler handler, AuthorMapper authorMapper) {
        this.handler = handler;
        this.authorMapper = authorMapper;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Authenticated
    @Path("/{systemId}/update-investment-state")
    public UpdatedSystemDTO updateInvestmentState(UpdateInvestmentStateCommandDTO dto,
                                                  @PathParam("systemId") int id,
                                                  @Context SecurityContext securityContext)
            throws NoSuchSystemException, CommandProcessingException {
        SR_Person author = authorMapper.fromSecurityContext(securityContext);
        UpdateInvestmentStateCommand command = dto.toCommand(id, author, Instant.now());

        var updatedSystemAndMetadata = handler.handle(command);

        return UpdatedSystemDTO.from(updatedSystemAndMetadata.getItem1(), updatedSystemAndMetadata.getItem2());
    }
}
