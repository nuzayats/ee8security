package ee8security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Collections;
import java.util.Set;

@ApplicationScoped
public class UsernamePasswordTotpIdentityStore implements IdentityStore {

    private final UserService userService;

    @Inject
    public UsernamePasswordTotpIdentityStore(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CredentialValidationResult validate(Credential c) {
        // Some hack due to this issue https://github.com/eclipse-ee4j/soteria/issues/192
        if (!(c instanceof UsernamePasswordTotpCredential)) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }

        UsernamePasswordTotpCredential credential = (UsernamePasswordTotpCredential) c;

        String username = credential.getUsernamePasswordCredential().getCaller();
        String password = credential.getUsernamePasswordCredential().getPasswordAsString();
        String totp = credential.getTotp();

        return userService.isTotpValid(username, totp) && userService.isUsernameAndPasswordValid(username, password)
                ? new CredentialValidationResult(username)
                : CredentialValidationResult.INVALID_RESULT;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return Collections.singleton(ValidationType.VALIDATE);
    }
}
