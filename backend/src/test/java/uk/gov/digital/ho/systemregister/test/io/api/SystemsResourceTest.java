package uk.gov.digital.ho.systemregister.test.io.api;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import uk.gov.digital.ho.systemregister.test.helpers.JSONFiles;
import uk.gov.digital.ho.systemregister.test.helpers.KeycloakServer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "CI", matches = "drone")
@QuarkusTestResource(KeycloakServer.class)
public class SystemsResourceTest {
    private static final Logger LOG = Logger.getLogger(SystemsResourceTest.class);
    JSONFiles resource = new JSONFiles();
    String bearerToken = "";

    @Test
    public void getSystems() {
        given().when().get("/api/systems").then().statusCode(200);
    }

    @Test
    @TestSecurity(authorizationEnabled = false, user = "Corey")
    public void AddSystem() {
        var cmd = resource.getAddSystemCommandJson();
        given().header("Content-Type", "application/json").body(cmd).when().post("/api/systems")
                .then().assertThat().statusCode(201);
        given().when().get("/api/systems").then().statusCode(200).body("systems.name",
                hasItems("Newly added system"));
    }

    @Test
    @TestSecurity(authorizationEnabled = false, user = "Corey")
    public void AddTwoSystems() {
        var cmd1 = resource.getAddSystemCommandJson();
        var cmd2 = resource.getAnotherAddSystemCommandJson();

        given().header("Content-Type", "application/json").body(cmd1).when().post("/api/systems")
                .then().assertThat().statusCode(201);
        given().header("Content-Type", "application/json").body(cmd2).when().post("/api/systems")
                .then().assertThat().statusCode(201);

        given().when().get("/api/systems").then().statusCode(200).body("systems.name",
                hasItems("Newly added system", "Yet another system"));
    }

    @Test
    public void AddSystem_Unauthorized() {
        weAreNotLoggedIn();
        given().header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + bearerToken).body("{}").when()
                .post("/api/systems").then().assertThat().statusCode(401);
    }

    private void weAreNotLoggedIn() {
        bearerToken = "";
    }
}
