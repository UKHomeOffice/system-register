package uk.gov.digital.ho.systemregister.test.io.api;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import uk.gov.digital.ho.systemregister.profiles.WithMockAuthorizationServer;
import uk.gov.digital.ho.systemregister.test.helpers.JSONFiles;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Inject;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static uk.gov.digital.ho.systemregister.test.io.api.JwtTokenBuilder.aJwtToken;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "CI", matches = "drone")
@TestProfile(WithMockAuthorizationServer.class)
public class SystemsResourceTest {
    private final JSONFiles resource = new JSONFiles();

    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    AgroalDataSource dataSource;

    @BeforeEach
    void cleanUpEventStore() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE eventstore.snapshots, eventstore.events;");
        }
    }

    @Test
    public void getSystems() {
        get("/api/systems").then().statusCode(200);
    }

    @Test
    @TestSecurity
    public void AddSystem() throws JSONException {
        String cmd = resource.getAddSystemCommandJson();
        String expectedResponse = getResourceAsString("system-response.json");
        given().auth().oauth2(
                aJwtToken()
                        .withFirstName("Betty")
                        .withSurname("Franklin")
                        .build())
                .contentType(JSON)
                .body(cmd)
                .when().post("/api/systems")
                .then().assertThat()
                .statusCode(201);

        String json = get("/api/systems").then()
                .statusCode(200)
                .and().extract()
                .response().asString();
        assertEquals(expectedResponse, json, false);
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

    @Test
    @TestSecurity
    public void updatesProductOwner() throws JSONException {
        String addSystemCommand = getResourceAsString("addSystemCommand.json");
        String updateProductOwnerCommand = getResourceAsString("update-product-owner/updateProductOwnerCommand.json");
        String expectedResponse = getResourceAsString("update-product-owner/updateProductOwnerSystemResponse.json");

        given().auth().oauth2(
                aJwtToken()
                        .withFirstName("Figgy")
                        .withSurname("Floofy")
                        .build())
                .contentType(JSON)
                .body(addSystemCommand)
                .when().post("/api/systems")
                .then().assertThat()
                .statusCode(201);

        String actualResponse = given().auth().oauth2(
                aJwtToken()
                        .withFirstName("Basil")
                        .withSurname("Barkley")
                        .build())
                .contentType(JSON)
                .body(updateProductOwnerCommand)
                .when().post("/api/systems/1/update-product-owner")
                .then().assertThat()
                .statusCode(200).and().extract().response().asString();

        assertEquals(expectedResponse, actualResponse, false);
    }
}
