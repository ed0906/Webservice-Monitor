package resource.dependency;

import java.net.HttpURLConnection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import model.WebserviceSummary;
import resource.Resource;
import util.Logger;
import api.MonitorAPI;

@Path("api/service/dependency")
public class GetDependencyResource extends Resource {

	@GET
	@Produces(MEDIA_TYPE)
	public Response getAllDependencies() {
		Logger.info("List Dependencies Request");
		
		MonitorAPI api = new MonitorAPI();
		try {
			List<WebserviceSummary> summary = api.getWebserviceSummaries();
			return Response.ok().entity(summary).header(ORIGIN, CLIENT).build();
		} catch (Exception e) {
			Logger.warning("List dependencies failed", e);
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).header(ORIGIN, CLIENT).build();
		}
	}
}
