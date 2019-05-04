package ee8security;

import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;

public class UsernamePasswordTotpCredential implements Credential {

    private final UsernamePasswordCredential usernamePasswordCredential;
    private final String totp;

    public UsernamePasswordTotpCredential(UsernamePasswordCredential usernamePasswordCredential, String totp) {
        this.usernamePasswordCredential = usernamePasswordCredential;
        this.totp = totp;
    }

    public UsernamePasswordCredential getUsernamePasswordCredential() {
        return usernamePasswordCredential;
    }

    public String getTotp() {
        return totp;
    }
}
