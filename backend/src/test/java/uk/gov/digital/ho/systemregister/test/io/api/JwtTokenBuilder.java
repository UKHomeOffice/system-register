package uk.gov.digital.ho.systemregister.test.io.api;

import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.*;

public class JwtTokenBuilder {
    String firstName = "FirstName";
    String surname = "Surname";
    String username = "test_Username";

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
		return null;
    }
    
    private String createJWT() {
  
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
    
        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("MTIzNDU2Nzg=");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId("id")
                .setIssuedAt(now)
                .setSubject("uid")
                .setIssuer("someone")
                .signWith(signatureAlgorithm, signingKey);
      
        //if it has been specified, let's add the expiration
        // if (ttlMillis > 0) {
        //     long expMillis = nowMillis + ttlMillis;
        //     Date exp = new Date(expMillis);
        //     builder.setExpiration(exp);
        // }  
      
        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
}
