package uk.gov.digital.ho.systemregister.test.io.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.InputStream;

import static io.restassured.RestAssured.get;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@Testcontainers
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

    private byte[] getResourceAsBytes(String resource) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream stream = classLoader.getResourceAsStream(resource)) {
            return toByteArray(requireNonNull(stream));
        } catch (IOException e) {
            throw new AssertionError("unable to load resource: " + resource, e);
        }
    }
}
