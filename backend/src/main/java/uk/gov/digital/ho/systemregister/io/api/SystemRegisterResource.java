package uk.gov.digital.ho.systemregister.io.api;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.messaging.SR_EventBus;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.NoSuchSystemException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.SystemNameNotUniqueException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.UpdateCriticalityCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.UpdateProductOwnerCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.UpdateSystemDescriptionCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.UpdateSystemNameCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateCriticalityCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateProductOwnerCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemDescriptionCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemNameCommand;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.api.dto.AddSystemCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.CurrentSystemStateDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.DtoMapper;
import uk.gov.digital.ho.systemregister.io.api.dto.UpdateCriticalityCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.UpdateProductOwnerCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.UpdateSystemDescriptionCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.UpdateSystemNameCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.UpdatedSystemDTO;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

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

    @Inject
    UpdateCriticalityCommandHandler updateCriticalityCommandHandler;

    @Inject
    UpdateSystemNameCommandHandler updateSystemNameCommandHandler;

    @Inject
    UpdateSystemDescriptionCommandHandler updateSystemDescriptionCommandHandler;

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
            public final String type = e.getClass().getSimpleName();
            public final SR_Event evt = e;
        }).collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Path("/{system_id}/update-name")
    public UpdatedSystemDTO updateSystemName(UpdateSystemNameCommandDTO cmd,
                                             @PathParam("system_id") int id,
                                             @Context SecurityContext securityContext)
            throws NoSuchSystemException, CommandHasNoEffectException, SystemNameNotUniqueException {
        SR_Person author = getAuthor(securityContext);
        UpdateSystemNameCommand command = DtoMapper.map(cmd, id, author, Instant.now());
        var updatedSystemAndMetadata = updateSystemNameCommandHandler.handle(command);

        return UpdatedSystemDTO.from(updatedSystemAndMetadata.getItem1(), updatedSystemAndMetadata.getItem2());
    }

    @POST
    @Path("/{system_id}/update-description")
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UpdatedSystemDTO updateSystemDescription(
            UpdateSystemDescriptionCommandDTO cmd,
            @PathParam("system_id") int id,
            @Context SecurityContext securityContext
    ) throws NoSuchSystemException, CommandHasNoEffectException {
        SR_Person author = getAuthor(securityContext);
        UpdateSystemDescriptionCommand command = DtoMapper.map(cmd, id, author, Instant.now());
        var updatedSystemAndMetadata = updateSystemDescriptionCommandHandler.handle(command);

        return UpdatedSystemDTO.from(updatedSystemAndMetadata.getItem1(), updatedSystemAndMetadata.getItem2());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Path("/{system_id}/update-criticality")
    public UpdatedSystemDTO updateCriticality(UpdateCriticalityCommandDTO cmd,
                                               @PathParam("system_id") int id,
                                               @Context SecurityContext securityContext)
            throws NoSuchSystemException, CommandHasNoEffectException {
        SR_Person author = getAuthor(securityContext);
        UpdateCriticalityCommand command = DtoMapper.map(cmd, id, author, Instant.now());
        var updatedSystemAndMetadata = updateCriticalityCommandHandler.handle(command);

        return UpdatedSystemDTO.from(updatedSystemAndMetadata.getItem1(), updatedSystemAndMetadata.getItem2());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Path("/{system_id}/update-product-owner")
    public UpdatedSystemDTO updateProductOwner(UpdateProductOwnerCommandDTO cmd,
                                               @PathParam("system_id") int id,
                                               @Context SecurityContext securityContext)
            throws NoSuchSystemException, CommandHasNoEffectException {
        SR_Person author = getAuthor(securityContext);
        UpdateProductOwnerCommand command = DtoMapper.map(cmd, id, author, Instant.now());
        var updatedSystemAndMetadata = updateProductOwnerCommandHandler.handle(command);

        return UpdatedSystemDTO.from(updatedSystemAndMetadata.getItem1(), updatedSystemAndMetadata.getItem2());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response addSystem(AddSystemCommandDTO cmd, @Context SecurityContext securityContext) {
        try {
            SR_Person author = getAuthor(securityContext);
            AddSystemCommand command = DtoMapper.map(cmd, author, Instant.now());
            eventBus.publish(command);
        } catch (Exception e) {
            LOG.error(e);
            return Response.serverError().build();
        }
        return Response.status(201).build();
    }

    private SR_Person getAuthor(SecurityContext securityContext) {
        JsonWebToken jwt = (JsonWebToken) securityContext.getUserPrincipal();
        return DtoMapper.extractAuthor(jwt);
    }
}
