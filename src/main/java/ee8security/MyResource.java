package ee8security;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/foo")
public class MyResource {

    @Inject
    private UserService userService;
    @Inject
    private BearerHandler bearerHandler;

    @GET
    @Path("/ping")
    public Response ping() {
        return Response.ok().entity("Service online").build();
    }

    @POST
    @Path("/createToken")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createToken(@Valid Input input) {
        UserService.Result auth = userService.auth(new UserCredential(input.companyId, input.email, input.password));

        if (auth == UserService.Result.SUCCESS) {
            Output output = new Output();
            output.token = bearerHandler.create(input.email);
            return Response.ok().entity(output).build();
        }

        if (auth == UserService.Result.OTP_REQUIRED) {
            return Response.status(Response.Status.BAD_REQUEST).entity("otp is needed").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity("bad credentials").build();
    }

    public static class Input {

        @NotNull
        public Long companyId;
        @NotNull
        public String email;
        @NotNull
        public String password;
        public String otp;
    }

    public static class Output {

        public String token;
    }
}
