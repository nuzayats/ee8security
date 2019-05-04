package ee8security;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.nio.charset.Charset;

@ApplicationScoped
public class SecretKeyProvider {

    @Produces
    SecretKey secretKey() {
        return Keys.hmacShaKeyFor("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".getBytes(Charset.forName("UTF-8")));
    }
}
