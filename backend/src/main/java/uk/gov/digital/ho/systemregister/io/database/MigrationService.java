package uk.gov.digital.ho.systemregister.io.database;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MigrationService {
    private static final Logger LOG = Logger.getLogger(MigrationService.class);

    @Inject
    Flyway flyway;

    public MigrationVersion migrate() {
        try {
            flyway.migrate();
        } catch (Exception e) {
            LOG.error(e);
        }
        LOG.info("Migrated DB to version: " + flyway.info().current().getVersion().toString());
        return flyway.info().current().getVersion();
    }

    public String getCurrentVersion() {
        return flyway.info().current().getVersion().getVersion();
    }
}