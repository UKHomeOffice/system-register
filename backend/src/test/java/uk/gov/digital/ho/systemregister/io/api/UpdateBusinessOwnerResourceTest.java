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
public class UpdateBusinessOwnerResourceTest extends ResourceTestBase {
    public UpdateBusinessOwnerResourceTest(@SuppressWarnings("CdiInjectionPointsInspection") AgroalDataSource dataSource) {
        super(dataSource);
    }

    @Test
    @TestSecurity
    public void updatesBusinessOwner() throws JSONException {
        String expectedResponse = getResourceAsString("update-business-owner/updateBusinessOwnerSystemResponse.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 200);

        String response = sendCommandToApi("update-business-owner/command.json", "/api/systems/1/update-business-owner", 200);

        assertEquals(expectedResponse, response, false);
        checkAllSystemsResponse("update-business-owner/expectedAllSystemsResponse.json");
    }

    @Test
    @TestSecurity
    void canSetBusinessOwnerToUnknown() throws JSONException {
        var expectedResponse = getResourceAsString("update-business-owner/minimal-update-response.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 200);

        var response = sendCommandToApi("update-business-owner/minimal-update-command.json", "/api/systems/1/update-business-owner", 200);

        assertEquals(expectedResponse, response, false);
    }

    @Test
    @TestSecurity
    public void mustBeAuthorised() {
        given().auth().none()
                .contentType(JSON)
                .body("{}")
                .when().post("/api/systems/1/update-business-owner")
                .then().assertThat().statusCode(401);
    }
}
