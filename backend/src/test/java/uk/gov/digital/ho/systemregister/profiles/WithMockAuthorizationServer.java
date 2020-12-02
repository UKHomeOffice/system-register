package uk.gov.digital.ho.systemregister.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;
import uk.gov.digital.ho.systemregister.test.helpers.MockAuthorizationServer;

import java.util.List;
import java.util.Map;

public class WithMockAuthorizationServer implements QuarkusTestProfile {
    @Override
    public String getConfigProfile() {
        return "test";
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of("quarkus.oidc.enabled", "true");
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return List.of(new TestResourceEntry(MockAuthorizationServer.class));
    }
}
