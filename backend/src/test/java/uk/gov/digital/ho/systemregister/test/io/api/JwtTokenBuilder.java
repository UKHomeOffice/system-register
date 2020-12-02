package uk.gov.digital.ho.systemregister.test.io.api;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.jsonwebtoken.security.Keys.secretKeyFor;

public class JwtTokenBuilder {
    private String firstName = "FirstName";
    private String surname = "Surname";
    private String username = "Corey";

    private JwtTokenBuilder() {
    }

    public static JwtTokenBuilder aJwtToken() {
        return new JwtTokenBuilder();
    }

    public JwtTokenBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public JwtTokenBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public JwtTokenBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public String build() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signingKey = secretKeyFor(signatureAlgorithm);

        JwtBuilder builder = Jwts.builder()
                .setId("id")
                .setIssuedAt(new Date())
                .setSubject("uid")
                .setIssuer("someone")
                .setAudience("system-register")
                .claim("realm_access", Map.of("roles", List.of("offline_access", "uma_authorization")))
                .claim("resource_access", Map.of("account", Map.of("roles", List.of("manage-account", "view-profile"))))
                .claim("scope", "openid profile email")
                .claim("preferred_username", username)
                .claim("given_name", firstName)
                .claim("family_name", surname)
                .claim("email", username + "@example.com")
                .signWith(signingKey, signatureAlgorithm);

        return builder.compact();
    }
}
