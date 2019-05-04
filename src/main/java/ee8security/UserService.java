package ee8security;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final Map<UserCredential, Boolean> VALID_CREDENTIAL_TO_OTP_ENABLED_FLAG_MAP;

    static {
        Map<UserCredential, Boolean> m = new HashMap<>();
        m.put(new UserCredential(1000, "john.doe@example.com", "john.doe.pw"), false);
        m.put(new UserCredential(2000, "jane.doe@example.com", "jane.doe.pw"), true);
        VALID_CREDENTIAL_TO_OTP_ENABLED_FLAG_MAP = Collections.unmodifiableMap(m);
    }

    enum Result {
        FAIL, OTP_REQUIRED, SUCCESS
    }

    Result auth(UserCredential userCredential) {
        Boolean otpEnabled = VALID_CREDENTIAL_TO_OTP_ENABLED_FLAG_MAP.get(userCredential);
        if (otpEnabled == null) return Result.FAIL;
        return otpEnabled ? Result.OTP_REQUIRED : Result.SUCCESS;
    }

    boolean isValid(UserCredential userCredential, String otp) {
        return false;
    }
}
