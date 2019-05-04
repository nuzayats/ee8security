package ee8security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Collections;
import java.util.Set;

@ApplicationScoped
public class UsernamePasswordIdentityStore implements IdentityStore {

    private final UserService userService;

    @Inject
    public UsernamePasswordIdentityStore(UserService userService) {
        this.userService = userService;
    }

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        if (userService.isTotpEnabled(credential.getCaller())) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }

        return userService.isUsernameAndPasswordValid(credential.getCaller(), credential.getPasswordAsString())
                ? new CredentialValidationResult(credential.getCaller())
                : CredentialValidationResult.INVALID_RESULT;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return Collections.singleton(ValidationType.VALIDATE);
    }
}
