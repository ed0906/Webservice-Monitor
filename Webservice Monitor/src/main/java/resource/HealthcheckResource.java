package resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class HealthcheckResource {

	private final static String MEDIA_TYPE = MediaType.APPLICATION_JSON;
	
	@GET
	@Produces(MEDIA_TYPE)
	public Response healthCheck() {
		return Response.ok().entity("The service is up and available").build();
	}
}
