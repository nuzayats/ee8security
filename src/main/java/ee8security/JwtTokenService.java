package ee8security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

class JwtTokenService {

    private static final Logger log = Logger.getLogger(JwtTokenService.class.getName());

    private final SecretKey secretKey;

    @Inject
    JwtTokenService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    String create(String subject) {
        return Jwts.builder() // TODO: add expiration
                .setSubject(subject)
                .signWith(secretKey)
                .compact();
    }

    Optional<String> verifyAndGetSubject(String token) {
        try {
            return Optional.of(Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (SignatureException | MalformedJwtException e) {
            log.log(Level.FINE, "error during parsing a token", e);
            return Optional.empty();
        }
    }
}
