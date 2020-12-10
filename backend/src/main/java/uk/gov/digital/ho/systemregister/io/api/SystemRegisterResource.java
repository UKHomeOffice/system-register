package uk.gov.digital.ho.systemregister.io.api;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.messaging.SR_EventBus;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.NoSuchSystemException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.UpdateProductOwnerCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.UpdateProductOwnerCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.api.dto.*;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Path("api/systems")
public class SystemRegisterResource {
    private static final Logger LOG = Logger.getLogger(SystemRegisterResource.class);

    @Inject
    @Named("postgres")
    IEventStore eventStore;

    @Inject
    SR_EventBus eventBus;

    @Inject
    CurrentSystemRegisterState systemRegisterState;

    @Inject
    UpdateProductOwnerCommandHandler updateProductOwnerCommandHandler;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CurrentSystemStateDTO systems() {
        var currentState = systemRegisterState.getCurrentState();
        return DtoMapper.map(currentState);
    }

    @GET
    @Path("/alpha/events")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public List<Object> events() {
        var events = eventStore.getEvents();
        return events.get().stream().map(e -> new Object() {
            public String type = e.getClass().getSimpleName();
            public SR_Event evt = e;
        }).collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Path("/{system_id}/update-product-owner")
    public UpdatedSystemDTO updateProductOwner(UpdateProductOwnerCommandDTO updateProductOwnerCommand,
                                               @PathParam("system_id") int id,
                                               @Context SecurityContext securityContext)
            throws NoSuchSystemException, CommandHasNoEffectException {
        JsonWebToken jwt = (JsonWebToken) securityContext.getUserPrincipal();
        Instant timestamp = Instant.now();
        SR_Person author = DtoMapper.extractAuthor(jwt);
        UpdateProductOwnerCommand command = DtoMapper.map(updateProductOwnerCommand, id, author, timestamp);
        var updatedSystemAndMetadata = updateProductOwnerCommandHandler.handle(command);

        return UpdatedSystemDTO.from(updatedSystemAndMetadata.getItem1(), updatedSystemAndMetadata.getItem2());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response addSystem(AddSystemCommandDTO cmd, @Context SecurityContext securityContext) {
        try {
            JsonWebToken jwt = (JsonWebToken) securityContext.getUserPrincipal();
            Instant timestamp = Instant.now();
            SR_Person author = DtoMapper.extractAuthor(jwt);
            AddSystemCommand command = DtoMapper.map(cmd, author, timestamp);
            eventBus.publish(command);
        } catch (Exception e) {
            LOG.error(e);
            return Response.serverError().build();
        }
        return Response.status(201).build();
    }
}