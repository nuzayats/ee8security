package ee8security;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/secured")
public class SecuredResource {

    @GET
    @Path("/greet")
    @Produces(MediaType.TEXT_PLAIN)
    public Response ping(@Context SecurityContext securityContext) {
        return Response.ok().entity("Hello " + securityContext.getUserPrincipal().getName()).build();
    }
}
