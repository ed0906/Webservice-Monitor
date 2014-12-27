package resource.service;

import java.net.HttpURLConnection;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import resource.Resource;
import model.Webservice;
import util.Logger;
import api.MonitorAPI;
import api.WebserviceValidator;

@Path("api/service/add")
public class AddServiceResource extends Resource{

	@POST
	@Produces(MEDIA_TYPE)
	public Response addWebservice(@QueryParam("service-name") String serviceName, @QueryParam("url") String serviceUrl){
		Logger.info("Add Service Request");
		Webservice service = new Webservice(serviceName, serviceUrl);
		
		int responseCode = WebserviceValidator.getResponseCode(service);
		if(responseCode == HttpURLConnection.HTTP_OK) {
			MonitorAPI api = new MonitorAPI();
			try {
				api.addWebservice(service);
			} catch (Exception e) {
				Logger.warning("Add service failed", e);
				return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).header(ORIGIN, CLIENT).build();
			}
			Logger.info("Added service " + serviceName + " at " + serviceUrl);
			return Response.ok().entity("Success").header(ORIGIN, CLIENT).build();
		}else{
			Logger.warning("Add service failed with response code: " + responseCode);
			return Response.status(responseCode).entity("Failed to add the webservice, it returned response: " + responseCode + ". Note you can only add a webservice that is currently up and available.").header(ORIGIN, CLIENT).build();
		}
	}
	
}
