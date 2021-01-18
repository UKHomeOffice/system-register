package uk.gov.digital.ho.systemregister.io.api;

import io.agroal.api.security.NamePrincipal;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import org.jose4j.jwt.JwtClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import javax.ws.rs.core.SecurityContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorMapperTest {
    private final SecurityContext context = mock(SecurityContext.class);

    private AuthorMapper mapper;

    @SuppressWarnings("SameParameterValue")
    private static DefaultJWTCallerPrincipal aToken(String username, String givenName, String familyName, String email) {
        var claims = new JwtClaims();
        claims.setStringClaim("preferred_username", username);
        claims.setStringClaim("given_name", givenName);
        claims.setStringClaim("family_name", familyName);
        claims.setStringClaim("email", email);
        return new DefaultJWTCallerPrincipal(claims);
    }

    @BeforeEach
    void setUp() {
        mapper = new AuthorMapper();
    }

    @Test
    void extractsAuthorFromJwt() {
        when(context.getUserPrincipal()).thenReturn(aToken("user", "Jo", "Smith", "jo@example.com"));

        SR_Person author = mapper.fromSecurityContext(context);

        assertThat(author).usingRecursiveComparison()
                .isEqualTo(new SR_Person("user", "Jo", "Smith", "jo@example.com"));
    }

    @Test
    void raisesExceptionIfSecurityContextDoesNotContainJsonWebToken() {
        when(context.getUserPrincipal()).thenReturn(new NamePrincipal("user"));

        assertThatThrownBy(() -> mapper.fromSecurityContext(context))
                .hasMessageContaining("not a bearer token")
                .isInstanceOf(UnauthorizedException.class);
    }
}
