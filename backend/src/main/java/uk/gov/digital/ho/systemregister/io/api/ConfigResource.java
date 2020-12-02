package uk.gov.digital.ho.systemregister.io.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;


@Path("/config")
public class ConfigResource {
    private static final Logger LOG = Logger.getLogger(ConfigResource.class);

    @ConfigProperty(name = "keycloak.host") 
    String host;

    @ConfigProperty(name = "keycloak.realm") 
    String realm;

    @ConfigProperty(name = "quarkus.oidc.client-id") 
    String clientId;

    @GET
    @Path("/keycloak")
    @Produces(MediaType.APPLICATION_JSON)
    public KeycloakConfig getConfig() {
        return new KeycloakConfig(host, realm, clientId);
    }
}
