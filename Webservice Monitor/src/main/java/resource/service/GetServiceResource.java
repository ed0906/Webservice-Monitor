package resource.service;

import java.net.HttpURLConnection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import model.WebserviceMetricUpdate;
import resource.Resource;
import util.Logger;
import api.MonitorAPI;

@Path("api/service/get")
public final class GetServiceResource extends Resource {
	
	@GET
	@Produces(MEDIA_TYPE)
	public Response getAllWebservices() {
		Logger.info("List Service Request");
		MonitorAPI api = new MonitorAPI();
		try {
			List<WebserviceMetricUpdate> services = api.getWebserviceList();
			return Response.ok(services).header(ORIGIN, CLIENT).build();
		} catch (Exception e) {
			Logger.warning("List services failed", e);
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(e).header(ORIGIN, CLIENT).build();
		}
	}
}
