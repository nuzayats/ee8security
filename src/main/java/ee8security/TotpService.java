package ee8security;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import javax.inject.Inject;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
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

    private final Clock clock;

    @Inject
    public TotpService(Clock clock) {
        this.clock = clock;
    }

    public String create(Key key) throws InvalidKeyException {
        int i = GENERATOR.generateOneTimePassword(key, new Date(clock.instant().toEpochMilli()));
        return String.format("%06d", i);
    }
}
