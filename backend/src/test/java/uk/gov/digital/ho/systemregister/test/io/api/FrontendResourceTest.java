package uk.gov.digital.ho.systemregister.test.io.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsBytes;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "CI", matches = "drone")
public class FrontendResourceTest {
    @Test
    void mapsTheRootPathToTheFrontendWebpage() {
        byte[] webPage = getResourceAsBytes("META-INF/resources/index.html");

        byte[] retrievedData = get("/").asByteArray();

        assertThat(retrievedData).containsExactly(webPage);
    }

    @Test
    void assumesUnmatchedPathsShouldBeRoutedToTheFrontendWebpage() {
        byte[] webPage = getResourceAsBytes("META-INF/resources/index.html");

        byte[] retrievedData = get("/systems/1").asByteArray();

        assertThat(retrievedData).containsExactly(webPage);
    }

    @Test
    void doesNotAnswerRequestsForOtherStaticResources() {
        byte[] staticResource = getResourceAsBytes("META-INF/resources/robots.txt");

        byte[] retrievedData = get("/robots.txt").asByteArray();

        assertThat(retrievedData).containsExactly(staticResource);
    }
}
