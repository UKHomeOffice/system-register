package uk.gov.digital.ho.systemregister;

import io.quarkus.runtime.ShutdownEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class Startup {
    private static final Logger LOG = Logger.getLogger(Startup.class);

    void onStop(@Observes ShutdownEvent ev) {
        LOG.info("The application is stopping...");
    }
}
