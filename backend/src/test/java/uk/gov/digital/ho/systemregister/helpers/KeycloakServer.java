package uk.gov.digital.ho.systemregister.helpers;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.util.Map;

import static java.lang.String.format;

public class KeycloakServer implements QuarkusTestResourceLifecycleManager {
    private static final String REALM_PATH = "/auth/realms/local-realm";
    private static final String SERVICE_NAME = "auth";
    private static final int SERVICE_PORT = 8080;

    private final DockerComposeContainer<?> container = new DockerComposeContainer<>(new File("compose/local-deps.yml"))
            .withServices(SERVICE_NAME)
            .withExposedService(SERVICE_NAME, SERVICE_PORT, Wait.forHttp(REALM_PATH));

    @Override
    public Map<String, String> start() {
        container.start();

        String host = container.getServiceHost(SERVICE_NAME, SERVICE_PORT);
        int port = container.getServicePort(SERVICE_NAME, SERVICE_PORT);

        return Map.of("quarkus.oidc.auth-server-url", format("http://%s:%d%s", host, port, REALM_PATH));
    }

    @Override
    public void stop() {
        container.stop();
    }
}
