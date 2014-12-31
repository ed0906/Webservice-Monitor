package resource.healthcheck;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import resource.Resource;

@Path("/")
public class HealthcheckResource extends Resource{
	
	@GET
	@Produces(MEDIA_TYPE)
	public Response healthCheck() {
		return Response.ok().entity("The service is up and available").header(ORIGIN, CLIENT).build();
	}
}
