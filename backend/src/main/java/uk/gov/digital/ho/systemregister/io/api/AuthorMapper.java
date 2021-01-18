package uk.gov.digital.ho.systemregister.io.api;

import io.quarkus.security.UnauthorizedException;
import org.eclipse.microprofile.jwt.JsonWebToken;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.security.Principal;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.SecurityContext;

@ApplicationScoped
public class AuthorMapper {
    public SR_Person fromSecurityContext(SecurityContext context) {
        JsonWebToken jwt = asJwt(context.getUserPrincipal());
        return new SR_Person(
                jwt.getName(),
                jwt.getClaim("given_name"),
                jwt.getClaim("family_name"),
                jwt.getClaim("email"));
    }

    private JsonWebToken asJwt(Principal principal) {
        try {
            return (JsonWebToken) principal;
        } catch (ClassCastException e) {
            throw new UnauthorizedException("principal is not a bearer token", e);
        }
    }
}
