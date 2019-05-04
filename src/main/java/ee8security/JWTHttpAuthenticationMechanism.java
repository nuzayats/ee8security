package ee8security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class JWTHttpAuthenticationMechanism implements HttpAuthenticationMechanism {

    private static final Pattern PATTERN = Pattern.compile("Bearer (.+)");

    private final BearerHandler bearerHandler;

    @Inject
    JWTHttpAuthenticationMechanism(BearerHandler bearerHandler) {
        this.bearerHandler = bearerHandler;
    }

    @Override
    public AuthenticationStatus validateRequest(
            HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext)
            throws AuthenticationException {

        Optional<String> bearer = Optional.ofNullable(
                request.getHeader(HttpHeaders.AUTHORIZATION)).flatMap(c -> {
            Matcher matcher = PATTERN.matcher(c);
            return matcher.find()
                    ? Optional.ofNullable(matcher.group(1))
                    : Optional.empty();
        });

        Optional<String> subject = bearer.flatMap(bearerHandler::getSubject);

        if (!subject.isPresent()) return httpMessageContext.doNothing();

        return httpMessageContext.notifyContainerAboutLogin(subject.get(), Collections.emptySet());
    }
}
