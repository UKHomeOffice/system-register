package uk.gov.digital.ho.systemregister.test.io.api;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import uk.gov.digital.ho.systemregister.profiles.WithMockAuthorizationServer;
import uk.gov.digital.ho.systemregister.test.helpers.JSONFiles;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.hasItems;
import static uk.gov.digital.ho.systemregister.test.io.api.JwtTokenBuilder.aJwtToken;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "CI", matches = "drone")
@TestProfile(WithMockAuthorizationServer.class)
public class SystemsResourceTest {
    private final JSONFiles resource = new JSONFiles();

    @Test
    public void getSystems() {
        get("/api/systems").then().statusCode(200);
    }

    @Test
    @TestSecurity
    public void AddSystem() {
        String cmd = resource.getAddSystemCommandJson();
        given().auth().oauth2(aJwtToken().build())
                .contentType(JSON)
                .body(cmd)
                .when().post("/api/systems")
                .then().assertThat()
                .statusCode(201);

        get("/api/systems").then()
                .statusCode(200)
                .body("systems.name", hasItems("Newly added system"));
    }

    @Test
    @Disabled("Try to get this working later")
    @TestSecurity
    public void AddSystem_RecordsAuthorsIdentity() {
        var cmd = resource.getAddSystemCommandJson();
        given().auth().oauth2(
                aJwtToken()
                        .withFirstName("Corey")
                        .withSurname("Mcvities")
                        .build())
                .contentType(JSON)
                .body(cmd)
                .when().post("/api/systems")
                .then().assertThat().statusCode(201);

        get("/api/systems").then()
                .statusCode(200)
                .body("systems.last_updated_by", hasItems("Corey Mcvities"));
    }

    @Test
    @TestSecurity
    public void AddTwoSystems() {
        var cmd1 = resource.getAddSystemCommandJson();
        var cmd2 = resource.getAnotherAddSystemCommandJson();

        given().auth().oauth2(aJwtToken().build())
                .contentType(JSON)
                .body(cmd1)
                .when().post("/api/systems")
                .then().assertThat()
                .statusCode(201);
        given().auth().oauth2(aJwtToken().build())
                .contentType(JSON)
                .body(cmd2)
                .when().post("/api/systems")
                .then().assertThat()
                .statusCode(201);

        get("/api/systems").then()
                .statusCode(200)
                .body("systems.name", hasItems("Newly added system", "Yet another system"));
    }

    @Test
    @TestSecurity
    public void AddSystem_Unauthorized() {
        given().auth().none()
                .contentType(JSON)
                .body("{}")
                .when().post("/api/systems")
                .then().assertThat().statusCode(401);
    }
}
