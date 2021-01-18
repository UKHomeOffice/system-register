package uk.gov.digital.ho.systemregister.helpers;

import org.json.JSONException;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static uk.gov.digital.ho.systemregister.io.api.JwtTokenBuilder.aJwtToken;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

public class HttpUtils {
    public static void checkAllSystemsResponse(String pathToExpectedJson) throws JSONException {
        String expectedAllSystemsResponse = getResourceAsString(pathToExpectedJson);

        String actualAllSystems = get("/api/systems").then()
                .statusCode(200)
                .and().extract()
                .response().asString();

        assertEquals(expectedAllSystemsResponse, actualAllSystems, false);
    }

    public static String sendCommandToApi(String pathToJson, String apiPath, int expectedStatusCode) {
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
