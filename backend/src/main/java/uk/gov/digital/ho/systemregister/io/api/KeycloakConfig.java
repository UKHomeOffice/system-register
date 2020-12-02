package uk.gov.digital.ho.systemregister.io.api;

public class KeycloakConfig {
    public final String host;
    public final String realm;
    public final String clientId;

    public KeycloakConfig(String host, String realm, String clientId) {
        this.host = host;
        this.realm = realm;
        this.clientId = clientId;
    }
}
