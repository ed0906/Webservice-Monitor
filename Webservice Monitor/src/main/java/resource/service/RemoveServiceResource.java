package resource.service;

import java.net.HttpURLConnection;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import resource.Resource;
import util.Logger;
import api.MonitorAPI;

@Path("api/service/delete")
public class RemoveServiceResource extends Resource {
	
	@POST
	@Produces(MEDIA_TYPE)
	public Response deleteWebservice(@QueryParam("service-name") String serviceName){
		Logger.info("Remove Service Request");
		MonitorAPI api = new MonitorAPI();
		if(!StringUtils.isEmpty(serviceName)){
			try {
				api.deleteWebservice(serviceName);
				Logger.info("Deleted webservice " + serviceName);
				return Response.ok().header(ORIGIN, CLIENT).build();
			} catch (Exception e) {
				Logger.warning("Failed to delete service " + serviceName, e);
			}
		}else{
			Logger.warning("Failed to delete service, query param 'service-name' not found");
		}
		return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Failed to remove the webservice").header(ORIGIN, CLIENT).build();
	}
}
