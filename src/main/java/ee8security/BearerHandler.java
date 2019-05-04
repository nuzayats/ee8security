package ee8security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import javax.inject.Inject;
import java.util.Optional;

class BearerHandler {

    private final SecretKey secretKey;

    @Inject
    BearerHandler(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    String create(String subject) {
        return Jwts.builder() // TODO: add expiration
                .setSubject(subject)
                .signWith(secretKey)
                .compact();
    }

    Optional<String> getSubject(String token) {
        try {
            return Optional.of(Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (SignatureException e) {
            return Optional.empty();
        }
    }
}
