package ee8security;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/secured")
public class SecuredResource {

    @GET
    @Path("/ping")
    public Response ping(@Context SecurityContext securityContext) {
        return Response.ok().entity("Hello " + securityContext.getUserPrincipal().getName()).build();
    }
}
