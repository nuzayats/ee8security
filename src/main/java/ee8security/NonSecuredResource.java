package ee8security;

import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult.Status;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;

@Path("/non-secured")
public class NonSecuredResource {

    @Inject
    private JwtTokenService jwtTokenService;
    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @POST
    @Path("/create-token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createToken(@Valid Input input) {
        if (input.totp != null) {
            Status status = identityStoreHandler.validate(
                    toCredential(input.email, input.password, input.totp)).getStatus();

            return status == VALID
                    ? createResponseWithToken(input.email)
                    : createBadCredentialResponse();
        }

        switch (identityStoreHandler.validate(toCredential(input.email, input.password)).getStatus()) {
            case VALID:
                return createResponseWithToken(input.email);
            case INVALID:
                return createBadCredentialResponse();
            case NOT_VALIDATED:
                return Response.status(Response.Status.BAD_REQUEST).entity("TOTP is needed; Try again with your TOTP").build();
            default:
                throw new RuntimeException();
        }
    }

    private Response createBadCredentialResponse() {
        return Response.status(Response.Status.BAD_REQUEST).entity("Bad credential").build();
    }

    private Response createResponseWithToken(String email) {
        Output output = new Output();
        output.token = jwtTokenService.create(email);
        return Response.ok().entity(output).build();
    }

    private Credential toCredential(String email, String password, String totp) {
        return new UsernamePasswordTotpCredential(toCredential(email, password), totp);
    }

    private static UsernamePasswordCredential toCredential(String email, String password) {
        return new UsernamePasswordCredential(email, password);
    }

    @SuppressWarnings("WeakerAccess")
    public static class Input {

        @NotNull
        public String email;
        @NotNull
        public String password;
        @Size(min = 6, max = 6)
        public String totp;
    }

    @SuppressWarnings("WeakerAccess")
    public static class Output {

        public String token;
    }
}
