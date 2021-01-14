package uk.gov.digital.ho.systemregister.io.api;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandProcessingException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.NoSuchSystemException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.UpdatePortfolioCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdatePortfolioCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.api.dto.DtoMapper;
import uk.gov.digital.ho.systemregister.io.api.dto.UpdatePortfolioCommandDTO;
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
public class UpdatePortfolioResource {
    private final UpdatePortfolioCommandHandler handler;

    public UpdatePortfolioResource(UpdatePortfolioCommandHandler handler) {
        this.handler = handler;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Authenticated
    @Path("/{systemId}/update-portfolio")
    public UpdatedSystemDTO updatePortfolio(UpdatePortfolioCommandDTO dto,
                                            @PathParam("systemId") int id,
                                            @Context SecurityContext securityContext)
            throws NoSuchSystemException, CommandProcessingException {
        SR_Person author = getAuthor(securityContext);
        UpdatePortfolioCommand command = dto.toCommand(id, author, Instant.now());

        var updatedSystemAndMetadata = handler.handle(command);

        return UpdatedSystemDTO.from(updatedSystemAndMetadata.getItem1(), updatedSystemAndMetadata.getItem2());
    }

    private SR_Person getAuthor(SecurityContext securityContext) {
        JsonWebToken jwt = (JsonWebToken) securityContext.getUserPrincipal();
        return DtoMapper.extractAuthor(jwt);
    }
}
