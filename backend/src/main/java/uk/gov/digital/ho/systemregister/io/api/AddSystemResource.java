package uk.gov.digital.ho.systemregister.io.api;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.AddSystemCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SystemNameNotUniqueException;
import uk.gov.digital.ho.systemregister.io.api.dto.AddSystemCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.DtoMapper;
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

    public AddSystemResource(AddSystemCommandHandler handler) {
        this.handler = handler;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Authenticated
    public UpdatedSystemDTO addSystem(
            AddSystemCommandDTO dto,
            @Context SecurityContext securityContext
    ) throws SystemNameNotUniqueException {
        SR_Person author = getAuthor(securityContext);
        AddSystemCommand command = dto.toCommand(author, Instant.now());

        var addedSystemAndMetadata = handler.handle(command);

        return UpdatedSystemDTO.from(addedSystemAndMetadata.getItem1(), addedSystemAndMetadata.getItem2());
    }

    private SR_Person getAuthor(SecurityContext securityContext) {
        JsonWebToken jwt = (JsonWebToken) securityContext.getUserPrincipal();
        return DtoMapper.extractAuthor(jwt);
    }
}
