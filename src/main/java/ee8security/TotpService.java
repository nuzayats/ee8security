package ee8security;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;

public class TotpService {

    private static final TimeBasedOneTimePasswordGenerator GENERATOR;

    static {
        try {
            GENERATOR = new TimeBasedOneTimePasswordGenerator();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String create(Key key) throws InvalidKeyException {
        int i = GENERATOR.generateOneTimePassword(key, new Date(Instant.now().toEpochMilli()));
        return String.format("%06d", i);
    }
}
