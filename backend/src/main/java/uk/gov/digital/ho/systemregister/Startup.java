package uk.gov.digital.ho.systemregister;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.messaging.SR_EventBus;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.AddSystemCommandHandler;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemAddedEventHandler;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class Startup {
    private static final Logger LOG = Logger.getLogger(Startup.class);

    @Inject
    SR_EventBus eventBus;

    @Inject
    @Named("postgres")
    IEventStore eventStore;

    @Inject
    CurrentSystemRegisterState systemRegisterState;

    void onStart(@Observes StartupEvent ev) {
        LOG.info("Wiring up listeners...");
        setupListeners();
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOG.info("The application is stopping...");
    }

    private void setupListeners() {
        eventBus.subscribe(new SystemAddedEventHandler(eventStore));
        eventBus.subscribe(new AddSystemCommandHandler(eventBus, systemRegisterState));
    }
}
