package ee8security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Collections;
import java.util.Set;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;

@ApplicationScoped
public class JwtTokenIdentityStore implements IdentityStore {

    private final JwtTokenService jwtTokenService;

    @Inject
    public JwtTokenIdentityStore(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public CredentialValidationResult validate(Credential c) {
        // Some hack due to this issue https://github.com/eclipse-ee4j/soteria/issues/192
        if (!(c instanceof JwtTokenCredential)) {
            return NOT_VALIDATED_RESULT;
        }

        JwtTokenCredential credential = (JwtTokenCredential) c;

        return jwtTokenService.verifyAndGetSubject(credential.getToken())
                .map(caller -> new CredentialValidationResult(caller, Collections.singleton("USER")))
                .orElse(INVALID_RESULT);
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return Collections.singleton(ValidationType.VALIDATE);
    }
}
