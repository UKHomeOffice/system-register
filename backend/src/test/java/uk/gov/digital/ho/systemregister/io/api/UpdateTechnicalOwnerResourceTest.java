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
public class UpdateTechnicalOwnerResourceTest extends ResourceTestBase {
    public UpdateTechnicalOwnerResourceTest(@SuppressWarnings("CdiInjectionPointsInspection") AgroalDataSource dataSource) {
        super(dataSource);
    }

    @Test
    @TestSecurity
    public void updatesTechnicalOwner() throws JSONException {
        String expectedResponse = getResourceAsString("update-technical-owner/updateTechnicalOwnerSystemResponse.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 200);

        String response = sendCommandToApi("update-technical-owner/command.json", "/api/systems/1/update-technical-owner", 200);

        assertEquals(expectedResponse, response, false);
        checkAllSystemsResponse("update-technical-owner/expectedAllSystemsResponse.json");
    }

    @Test
    @TestSecurity
    public void mustBeAuthorised() {
        given().auth().none()
                .contentType(JSON)
                .body("{}")
                .when().post("/api/systems/1/update-technical-owner")
                .then().assertThat().statusCode(401);
    }
}
