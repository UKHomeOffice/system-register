package uk.gov.digital.ho.systemregister.io.api;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.messaging.SR_EventBus;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.*;
import uk.gov.digital.ho.systemregister.application.messaging.commands.*;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.io.api.dto.*;
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
    @Path("/{system_id}/update-product-owner")
    public UpdatedSystemDTO updateProductOwner(UpdateProductOwnerCommandDTO cmd,
                                               @PathParam("system_id") int id,
                                               @Context SecurityContext securityContext)
            throws NoSuchSystemException, CommandProcessingException {
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
