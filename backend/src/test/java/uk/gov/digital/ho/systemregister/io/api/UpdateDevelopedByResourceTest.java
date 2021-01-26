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
public class UpdateDevelopedByResourceTest extends ResourceTestBase {

    public UpdateDevelopedByResourceTest(@SuppressWarnings("CdiInjectionPointsInspection") AgroalDataSource dataSource) {
        super(dataSource);
    }

    @Test
    @TestSecurity
    public void updatesDevelopedBy() throws JSONException {
        String expectedResponse = getResourceAsString("update-developed-by/updateDevelopedBySystemResponse.json");
        sendCommandToApi("add-system/addSystemCommand.json", "/api/systems", 201);

        String response = sendCommandToApi("update-developed-by/command.json", "/api/systems/1/update-developed-by", 200);

        assertEquals(expectedResponse, response, false);
        checkAllSystemsResponse("update-developed-by/expectedAllSystemsResponse.json");
    }

    @Test
    @TestSecurity
    public void mustBeAuthorised() {
        given().auth().none()
                .contentType(JSON)
                .body("{}")
                .when().post("/api/systems/1/update-developed-by")
                .then().assertThat().statusCode(401);
    }
}
