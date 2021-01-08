package uk.gov.digital.ho.systemregister.io.api;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import uk.gov.digital.ho.systemregister.profiles.WithMockAuthorizationServer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Inject;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static uk.gov.digital.ho.systemregister.io.api.JwtTokenBuilder.aJwtToken;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "CI", matches = "drone")
@TestProfile(WithMockAuthorizationServer.class)
public class SystemsResourceTest {
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
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 201);
        checkAllSystemsResponse("add-system/system-response.json");
    }

    @Test
    @TestSecurity
    public void AddTwoSystems() {
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 201);
        sendCommandToApi("add-system/addAnotherSystemCommand.json", "/api/systems", 201);

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
        String expectedResponse = getResourceAsString("update-product-owner/updateProductOwnerSystemResponse.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 201);

        String actualResponse = sendCommandToApi("update-product-owner/command.json", "/api/systems/1/update-product-owner", 200);

        assertEquals(expectedResponse, actualResponse, false);
        checkAllSystemsResponse("update-product-owner/expectedAllSystemsResponse.json");
    }

    @Test
    @TestSecurity
    public void updatesCriticality() throws JSONException {
        String expectedResponse = getResourceAsString("update-criticality/expectedResponse.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 201);

        String actualResponse = sendCommandToApi("update-criticality/command.json", "/api/systems/1/update-criticality", 200);

        assertEquals(expectedResponse, actualResponse, false);
        checkAllSystemsResponse("update-criticality/expectedAllSystemsResponse.json");
    }

    @Test
    @TestSecurity
    public void updatesSystemName() throws JSONException {
        String expectedResponse = getResourceAsString("update-name/expectedResponse.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 201);

        String actualResponse = sendCommandToApi("update-name/command.json", "/api/systems/1/update-name", 200);

        assertEquals(expectedResponse, actualResponse, false);
        checkAllSystemsResponse("update-name/expectedAllSystemsResponse.json");
    }

    @Test
    @TestSecurity
    public void updatesSystemDescription() throws JSONException {
        String expectedResponse = getResourceAsString("update-system-description/update-system-description-response.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 201);

        String actualResponse = sendCommandToApi("update-system-description/command.json", "/api/systems/1/update-description", 200);

        assertEquals(expectedResponse, actualResponse, false);
        checkAllSystemsResponse("update-system-description/all-systems-response.json");
    }

    private void checkAllSystemsResponse(String pathToExpectedJson) throws JSONException {
        String expectedAllSystemsResponse = getResourceAsString(pathToExpectedJson);

        String actualAllSystems = get("/api/systems").then()
                .statusCode(200)
                .and().extract()
                .response().asString();

        assertEquals(expectedAllSystemsResponse, actualAllSystems, false);
    }

    private String sendCommandToApi(String pathToJson, String apiPath, int expectedStatusCode) {
        String command = getResourceAsString(pathToJson);

        String actualResponse = given().auth().oauth2(
                aJwtToken()
                        .withFirstName("Basil")
                        .withSurname("Barkley")
                        .build())
                .contentType(JSON)
                .body(command)
                .when().post(apiPath)
                .then().assertThat()
                .statusCode(expectedStatusCode).and().extract().response().asString();

        return actualResponse;
    }
}
