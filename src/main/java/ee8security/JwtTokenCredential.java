package ee8security;

import javax.security.enterprise.credential.Credential;

class JwtTokenCredential implements Credential {

    private final String token;

    JwtTokenCredential(String token) {
        this.token = token;
    }

    String getToken() {
        return token;
    }
}
