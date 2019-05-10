package ee8security;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/secured")
public class SecuredResource {

    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    private SecurityContext securityContext;

    @GET
    @Path("/greet")
    @RolesAllowed({"USER"})
    @Produces(MediaType.TEXT_PLAIN)
    public Response greet() {
        return Response.ok().entity("Hello " + securityContext.getCallerPrincipal().getName()).build();
    }
}
