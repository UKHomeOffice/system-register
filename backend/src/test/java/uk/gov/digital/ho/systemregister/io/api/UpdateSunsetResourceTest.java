package uk.gov.digital.ho.systemregister.io.api;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import uk.gov.digital.ho.systemregister.profiles.WithMockAuthorizationServer;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static uk.gov.digital.ho.systemregister.helpers.HttpUtils.checkAllSystemsResponse;
import static uk.gov.digital.ho.systemregister.helpers.HttpUtils.sendCommandToApi;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "CI", matches = "drone")
@TestProfile(WithMockAuthorizationServer.class)
public class UpdateSunsetResourceTest extends ResourceTestBase {
    public UpdateSunsetResourceTest(@SuppressWarnings("CdiInjectionPointsInspection") AgroalDataSource dataSource) {
        super(dataSource);
    }

    @Test
    @TestSecurity
    public void updatesSunset() throws JSONException {
        String expectedResponse = getResourceAsString("update-sunset/updateSunsetSystemResponse.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 200);

        String response = sendCommandToApi("update-sunset/command.json", "/api/systems/1/update-sunset", 200);

        assertEquals(expectedResponse, response, false);
        checkAllSystemsResponse("update-sunset/expectedAllSystemsResponse.json");
    }

    @Test
    @TestSecurity
    void canSetSunsetToUnknown() throws JSONException {
        var expectedResponse = getResourceAsString("update-sunset/minimal-update-response.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 200);
        sendCommandToApi("update-sunset/command.json", "/api/systems/1/update-sunset", 200);

        var response = sendCommandToApi("update-sunset/minimal-update-command.json", "/api/systems/1/update-sunset", 200);

        assertEquals(expectedResponse, response, false);
    }

    @Test
    @TestSecurity
    public void mustBeAuthorised() {
        given().auth().none()
                .contentType(JSON)
                .body("{}")
                .when().post("/api/systems/1/update-sunset")
                .then().assertThat().statusCode(401);
    }
}
