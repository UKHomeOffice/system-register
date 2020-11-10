package uk.gov.digital.ho.systemregister.test.io.database;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import uk.gov.digital.ho.systemregister.io.database.MigrationService;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@DisabledIfEnvironmentVariable(named="CI", matches="drone")
public class MigrationTest {
    private static final String ExpectedDBVersion = "1.0.0";

    @Inject
    MigrationService migrationService;

    @Test
    public void TestMigration() {
        migrationService.migrate();

        assertEquals(ExpectedDBVersion, migrationService.getCurrentVersion());
    }
}
