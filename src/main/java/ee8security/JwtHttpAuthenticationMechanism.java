package ee8security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;

@ApplicationScoped
public class JwtHttpAuthenticationMechanism implements HttpAuthenticationMechanism {

    private static final Pattern PATTERN = Pattern.compile("Bearer (.+)");

    private final IdentityStoreHandler identityStoreHandler;

    @Inject
    JwtHttpAuthenticationMechanism(
            @SuppressWarnings("CdiInjectionPointsInspection") IdentityStoreHandler identityStoreHandler) {
        this.identityStoreHandler = identityStoreHandler;
    }

    @Override
    public AuthenticationStatus validateRequest(
            HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        Optional<String> bearer = getBearer(request);

        if (bearer.isPresent()) {
            CredentialValidationResult result = identityStoreHandler.validate(new JwtTokenCredential(bearer.get()));
            if (result.getStatus() == VALID) {
                return httpMessageContext.notifyContainerAboutLogin(result);
            }
        }

        return httpMessageContext.doNothing();
    }

    private static Optional<String> getBearer(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).flatMap(c -> {
            Matcher matcher = PATTERN.matcher(c);
            return matcher.find()
                    ? Optional.ofNullable(matcher.group(1))
                    : Optional.empty();
        });
    }
}
