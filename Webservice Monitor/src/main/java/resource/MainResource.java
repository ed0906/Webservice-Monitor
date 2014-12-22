package resource;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Webservice;
import util.Logger;
import api.Metric;
import api.MonitorAPI;
import api.WebserviceValidator;

@Path("/api")
public final class MainResource {
	
	private final static String MEDIA_TYPE = MediaType.APPLICATION_JSON;
	private final static String ORIGIN = "Access-Control-Allow-Origin";
	private final static String CLIENT = "*";

	@GET
	@Path("/healthcheck")
	@Produces(MEDIA_TYPE)
	public Response healthCheck() {
		return Response.ok().build();
	}
	
	@GET
	@Path("/service")
	@Produces(MEDIA_TYPE)
	public Response listWebservices() {
		Logger.info("List Service Request");
		MonitorAPI api = new MonitorAPI();
		try {
			List<Webservice> services = api.getWebserviceList();
			return Response.ok(services).header(ORIGIN, CLIENT).build();
		} catch (Exception e) {
			Logger.warning("List services failed", e);
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(e).header(ORIGIN, CLIENT).build();
		}
	}
	
	@GET
	@Path("/service/{serviceName}")
	@Produces(MEDIA_TYPE)
	public Response getWebservice(@PathParam("serviceName") String serviceName) {
		Logger.info("Get Service Request");
		MonitorAPI api = new MonitorAPI();		
		try {
			Webservice service = api.getWebservice(serviceName);
			return Response.ok(service).header(ORIGIN, CLIENT).build();
		} catch (Exception e) {
			Logger.warning("Get service failed", e);
			return Response.status(HttpURLConnection.HTTP_NO_CONTENT).entity("No such service").header(ORIGIN, CLIENT).build();
		}
	}
	
	@GET
	@Path("/service/{serviceName}/update")
	@Produces(MEDIA_TYPE)
	public Response getWebserviceUpdate(@PathParam("serviceName") String serviceName) {
		Logger.info("Update Service Request");
		MonitorAPI api = new MonitorAPI();		
		try {
			Map<Metric, Number> metricMap = api.getUpdate(serviceName, Metric.values());
			return Response.ok(metricMap).header(ORIGIN, CLIENT).build();
		} catch (Exception e) {
			Logger.warning("Update service failed", e);
			return Response.status(HttpURLConnection.HTTP_NO_CONTENT).entity("No such service").header(ORIGIN, CLIENT).build();
		}
	}
	
	@POST
	@Path("service")
	@Produces(MEDIA_TYPE)
	public Response addWebservice(@QueryParam("service-name") String serviceName, @QueryParam("url") String serviceUrl){
		//String serviceName = form.getFirst("service-name");
		//String serviceUrl = form.getFirst("url");
		
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
			return Response.ok().entity("Success").header(ORIGIN, CLIENT).build();
		}else{
			Logger.warning("Add service failed with response code: " + responseCode);
			return Response.status(responseCode).entity("Failed to add the webservice, returned response: " + responseCode).header(ORIGIN, CLIENT).build();
		}
	}
}
