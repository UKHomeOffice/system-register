package uk.gov.digital.ho.systemregister.io.api;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.messaging.SR_EventBus;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.io.api.dto.AddSystemCommandDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.DtoMapper;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import uk.gov.digital.ho.systemregister.io.api.dto.SnapshotDTO;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

@Path("api/systems")
public class SystemRegisterResource {
    private static final Logger LOG = Logger.getLogger(SystemRegisterResource.class);

    @Inject
    SecurityIdentity identity;

    @Inject
    @Named("postgres")
    IEventStore eventStore;

    @Inject
    SR_EventBus eventBus;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SnapshotDTO systems() {
        var snapshot = new CurrentSystemRegisterState(eventStore).getSystems();
        return DtoMapper.map(snapshot);
    }

    @GET
    @Path("/alpha/events")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public List<Object> events() {
        var events = eventStore.getEvents();
        return events.get().stream().map(e -> new Object(){
            public String type = e.getClass().getSimpleName();
            public SR_Event evt = e;
        }).collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response addSystem(AddSystemCommandDTO cmd) {
        try {
            String author = identity.getPrincipal().getName();
            Instant timestamp = Instant.now();
            AddSystemCommand command = DtoMapper.map(cmd, author, timestamp);
            eventBus.publish(command);
        } catch (Exception e) {
            LOG.error(e);
            return Response.serverError().build();
        }

        return Response.status(201).build();
    }
}
