package ee8security;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MockUserService implements UserService {

    private static final Map<String, String> USER_PASSWORD_MAP;

    static {
        Map<String, String> m = new HashMap<>();
        m.put("john.doe@example.com", "john.doe.pw"); // must not be clear text like this..
        m.put("jane.doe@example.com", "jane.doe.pw");
        USER_PASSWORD_MAP = Collections.unmodifiableMap(m);
    }

    private static final Key JOHN_SHARED_KEY_FOR_TOTP = base32ToKey("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

    private static final Map<String, Key> USER_KEY_MAP;

    static {
        Map<String, Key> m = new HashMap<>();
        m.put("john.doe@example.com", JOHN_SHARED_KEY_FOR_TOTP);
        USER_KEY_MAP = Collections.unmodifiableMap(m);
    }

    private final TotpService totpService;

    @Inject
    public MockUserService(TotpService totpService) {
        this.totpService = totpService;
    }

    @Override
    public boolean isUsernameAndPasswordValid(String username, String password) {
        String expectedPassword = USER_PASSWORD_MAP.get(username);
        return expectedPassword != null && expectedPassword.equals(password);
    }

    @Override
    public boolean isTotpEnabled(String username) {
        return USER_KEY_MAP.containsKey(username);
    }

    @Override
    public boolean isTotpValid(String username, String totp) {
        Key key = USER_KEY_MAP.get(username);
        return key != null && createTotp(key).equals(totp);
    }

    private String createTotp(Key key) {
        try {
            return totpService.create(key);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private static Key base32ToKey(@SuppressWarnings("SameParameterValue") String base32) {
        assert base32.length() == 32;
        byte[] b = new Base32().decode(base32);
        assert b.length == 20;
        return new SecretKeySpec(b, 0, b.length, "SHA1");
    }
}
