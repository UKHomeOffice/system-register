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
public class UpdatePublicFacingResourceTest extends ResourceTestBase {

    public UpdatePublicFacingResourceTest(@SuppressWarnings("CdiInjectionPointsInspection") AgroalDataSource dataSource) {
        super(dataSource);
    }

    @Test
    @TestSecurity
    public void updatesPublicFacing() throws JSONException {
        String expectedResponse = getResourceAsString("update-public-facing/expectedResponse.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 200);

        String response = sendCommandToApi("update-public-facing/command.json", "/api/systems/1/update-public-facing", 200);

        assertEquals(expectedResponse, response, false);
        checkAllSystemsResponse("update-public-facing/expectedAllSystemsResponse.json");
    }

    @Test
    @TestSecurity
    void canSetPublicFacingToUnknown() throws JSONException {
        var expectedResponse = getResourceAsString("update-public-facing/minimal-update-response.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 200);
        sendCommandToApi("update-public-facing/command.json", "/api/systems/1/update-public-facing", 200);

        var response = sendCommandToApi("update-public-facing/minimal-update-command.json", "/api/systems/1/update-public-facing", 200);

        assertEquals(expectedResponse, response, false);
    }

    @Test
    @TestSecurity
    public void mustBeAuthorised() {
        given().auth().none()
                .contentType(JSON)
                .body("{}")
                .when().post("/api/systems/1/update-public-facing")
                .then().assertThat().statusCode(401);
    }
}
