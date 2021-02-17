package uk.gov.digital.ho.systemregister.io.api;

import io.quarkus.security.Authenticated;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.io.api.dto.CurrentSystemStateDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.DtoMapper;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("api/systems")
public class SystemRegisterResource {
    @Inject
    @Named("postgres")
    IEventStore eventStore;

    @Inject
    CurrentSystemRegisterState systemRegisterState;

    @GET
    @Produces(APPLICATION_JSON)
    public CurrentSystemStateDTO systems() {
        var currentState = systemRegisterState.getCurrentState();
        return DtoMapper.map(currentState);
    }

    @GET
    @Path("/alpha/events")
    @Produces(APPLICATION_JSON)
    @Authenticated
    public List<Object> events() {
        var events = eventStore.getEvents();
        return events.get().stream().map(e -> new Object() {
            public final String type = e.getClass().getSimpleName();
            public final SR_Event evt = e;
        }).collect(Collectors.toList());
    }
}
