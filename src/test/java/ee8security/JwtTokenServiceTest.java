package ee8security;

import io.jsonwebtoken.security.Keys;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JwtTokenServiceTest {

    private final JwtTokenService sut
            = new JwtTokenService(Keys.hmacShaKeyFor("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".getBytes(Charset.forName("UTF-8"))));

    @Test
    public void create() {
        String token = sut.create("foo");

        assertEquals("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb28ifQ.i5Uh9hqaKyNzjgjxOFkF3DHP_pcsZ6UsZYMNbLYRj1A", token);
    }

    @Test
    public void valid() {
        String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb28ifQ.i5Uh9hqaKyNzjgjxOFkF3DHP_pcsZ6UsZYMNbLYRj1A";

        Optional<String> s = sut.verifyAndGetSubject(validToken);

        assertEquals("foo", s.get());
    }

    @Test
    public void invalid() {
        String temperedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiYXIifQ.i5Uh9hqaKyNzjgjxOFkF3DHP_pcsZ6UsZYMNbLYRj1A";

        Optional<String> result = sut.verifyAndGetSubject(temperedToken);

        assertFalse(result.isPresent());
    }
}
