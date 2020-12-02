package uk.gov.digital.ho.systemregister.test.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.time.Instant;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

public class MockAuthorizationServer implements QuarkusTestResourceLifecycleManager {
    private static WireMockServer wireMockServer;

    private static JsonNode buildConfiguration(WireMockServer wireMockServer) {
        return JsonNodeFactory.instance.objectNode()
                .put("token_introspection_endpoint", wireMockServer.baseUrl() + "/introspect");
    }

    private static JsonNode buildJwt() {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(60);
        return JsonNodeFactory.instance.objectNode()
                .put("active", true)
                .put("scope", "openid profile email")
                .put("preferred_username", "test_user")
                .put("iat", issuedAt.getEpochSecond())
                .put("exp", expiresAt.getEpochSecond());
    }

    @Override
    public Map<String, String> start() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer();
            wireMockServer.start();
        }

        stubFor(get("/.well-known/openid-configuration")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(buildConfiguration(wireMockServer))));
        stubFor(post("/introspect")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(buildJwt())));

        return Map.of("quarkus.oidc.auth-server-url", wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }
}
