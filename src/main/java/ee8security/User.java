package ee8security;

public class User {

    final UserCredential userCredential;
    final boolean otpEnabled;

    public User(UserCredential userCredential, boolean otpEnabled) {
        this.userCredential = userCredential;
        this.otpEnabled = otpEnabled;
    }
}
