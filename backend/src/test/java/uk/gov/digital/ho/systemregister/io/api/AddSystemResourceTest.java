package uk.gov.digital.ho.systemregister.io.api;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import uk.gov.digital.ho.systemregister.profiles.WithMockAuthorizationServer;

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
class AddSystemResourceTest extends ResourceTestBase {
    @SuppressWarnings("CdiInjectionPointsInspection")
    public AddSystemResourceTest(AgroalDataSource dataSource) {
        super(dataSource);
    }

    @Test
    @TestSecurity
    public void addsSystems() throws JSONException {
        String expectedResponse = getResourceAsString("add-system/expectedResponse.json");

        String response = sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 200);

        assertEquals(expectedResponse, response, false);
        checkAllSystemsResponse("add-system/system-response.json");
    }

    @Test
    @TestSecurity
    public void multipleSystemsCanBeAddedIndependently() {
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 200);
        sendCommandToApi("add-system/addAnotherSystemCommand.json", "/api/systems", 200);

        get("/api/systems").then()
                .statusCode(200)
                .body("systems.name", hasItems("Newly added system", "Yet another system"));
    }

    @Test
    @TestSecurity
    public void duplicateSystemsCanNotBeAdded() throws JSONException {
        String expectedResponse = getResourceAsString("add-system/duplicate-system-response.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 200);

        String response = sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 400);

        assertEquals(expectedResponse, response, true);
    }

    @Test
    @TestSecurity
    public void mustBeAuthorisedToAddSystems() {
        given().auth().none()
                .contentType(JSON)
                .body("{}")
                .when().post("/api/systems")
                .then().assertThat().statusCode(401);
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

        return given().auth().oauth2(
                aJwtToken()
                        .withFirstName("Basil")
                        .withSurname("Barkley")
                        .build())
                .contentType(JSON)
                .body(command)
                .when().post(apiPath)
                .then().assertThat()
                .statusCode(expectedStatusCode).and().extract().response().asString();
    }
}
