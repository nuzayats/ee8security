package ee8security;

public interface UserService {

    boolean isUsernameAndPasswordValid(String username, String password);

    boolean isTotpEnabled(String username);

    boolean isTotpValid(String username, String totp);
}
