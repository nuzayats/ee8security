package ee8security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import javax.inject.Inject;
import java.time.Clock;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

class JwtTokenService {

    private static final Logger log = Logger.getLogger(JwtTokenService.class.getName());
    private static final Duration EXPIRATION = Duration.ofMinutes(30);

    private final SecretKey secretKey;
    private final Clock clock;

    @Inject
    JwtTokenService(SecretKey secretKey, Clock clock) {
        this.secretKey = secretKey;
        this.clock = clock;
    }

    String create(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .signWith(secretKey)
                .setExpiration(new Date(clock.instant().plus(EXPIRATION).toEpochMilli()))
                .compact();
    }

    Optional<String> verifyAndGetSubject(String token) {
        try {
            return Optional.of(Jwts
                    .parser()
                    .setClock(() -> new Date(clock.instant().toEpochMilli()))
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException e) {
            log.log(Level.FINE, "error during parsing a token", e);
            return Optional.empty();
        }
    }
}
