package ee8security;

import io.jsonwebtoken.security.Keys;
import org.junit.Test;

import java.nio.charset.Charset;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JwtTokenServiceTest {

    private final JwtTokenService sut = new JwtTokenService(
            Keys.hmacShaKeyFor("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".getBytes(Charset.forName("UTF-8"))),
            Clock.fixed(Instant.ofEpochSecond(1556712000L), ZoneId.of("UTC")));

    @Test
    public void create() {
        String token = sut.create("foo");

        assertEquals("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb28iLCJleHAiOjE1NTY3MTM4MDB9.n31gdm7sKDnvsUYS-WCQ4IHkTJYF6VkfiVCShJrUOBQ", token);
    }

    @Test
    public void valid() {
        String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb28iLCJleHAiOjE1NTY3MTM4MDB9.n31gdm7sKDnvsUYS-WCQ4IHkTJYF6VkfiVCShJrUOBQ";

        Optional<String> s = sut.verifyAndGetSubject(validToken);

        assertEquals("foo", s.get());
    }

    @Test
    public void invalid() {
        String temperedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiYXIiLCJleHAiOjE1NTY3MTM4MDB9.3_ByeOWIpZUtTSpH8JTGWG7nMMLrKPa1hEPdfxz9pPg";

        Optional<String> result = sut.verifyAndGetSubject(temperedToken);

        assertFalse(result.isPresent());
    }

    @Test
    public void expired() {
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb28iLCJleHAiOjE1NTY3MTE5OTl9.92l4ddrw_dhAVmDutgY4W6a2moJGDl1KTKkDzT8XtII";

        Optional<String> result = sut.verifyAndGetSubject(expiredToken);

        assertFalse(result.isPresent());
    }
}
