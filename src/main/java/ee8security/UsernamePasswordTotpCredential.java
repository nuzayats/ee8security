package ee8security;

import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;

class UsernamePasswordTotpCredential implements Credential {

    private final UsernamePasswordCredential usernamePasswordCredential;
    private final String totp;

    UsernamePasswordTotpCredential(UsernamePasswordCredential usernamePasswordCredential, String totp) {
        this.usernamePasswordCredential = usernamePasswordCredential;
        this.totp = totp;
    }

    UsernamePasswordCredential getUsernamePasswordCredential() {
        return usernamePasswordCredential;
    }

    String getTotp() {
        return totp;
    }
}
