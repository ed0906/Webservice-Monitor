package resource;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.MetricSet;
import model.Webservice;
import model.WebserviceOverview;

import org.apache.commons.lang3.StringUtils;

import util.Logger;
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
			List<WebserviceOverview> services = api.getWebserviceList();
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
			MetricSet metrics = api.getUpdate(serviceName);
			return Response.ok(metrics).header(ORIGIN, CLIENT).build();
		} catch (Exception e) {
			Logger.warning("Update service failed", e);
			return Response.status(HttpURLConnection.HTTP_NO_CONTENT).entity("No such service").header(ORIGIN, CLIENT).build();
		}
	}
	
	@GET
	@Path("/service/{serviceName}/metrics")
	public Response getMetrics(@PathParam("serviceName") String serviceName, @QueryParam("from") String from, @QueryParam("until") String until){
		Logger.info("Update Service Request");
		MonitorAPI api = new MonitorAPI();
		
		try{
			Date dateUntil;
			Date dateFrom;
			if(StringUtils.isEmpty(until) && StringUtils.isEmpty(from)){
				MetricSet metrics = api.getMetrics(serviceName);
				return Response.ok(metrics).header(ORIGIN, CLIENT).build();
			}else{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(StringUtils.isEmpty(until)){
					dateUntil = new Date();
				}else{
					dateUntil = format.parse(until);
				}
				if(StringUtils.isEmpty(from)){
					dateFrom = new Date(System.currentTimeMillis()-24*3600000);
				}else{
					dateFrom = format.parse(from);
				}
				List<MetricSet> metrics = api.getMetrics(serviceName, dateFrom, dateUntil);
				return Response.ok(metrics).header(ORIGIN, CLIENT).build();
			}
		}catch(Exception e){
			Logger.error("Failed to get metrics",e);
			return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).header(ORIGIN, CLIENT).build();
		}
	}
	
	@POST
	@Path("service")
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
			return Response.status(responseCode).entity("Failed to add the webservice, returned response: " + responseCode).header(ORIGIN, CLIENT).build();
		}
	}
	
	@POST
	@Path("service/delete")
	@Produces(MEDIA_TYPE)
	public Response removeWebservice(@QueryParam("service-name") String serviceName){
		Logger.info("Remove Service Request");
		MonitorAPI api = new MonitorAPI();
		try {
			api.deleteWebservice(serviceName);
			Logger.info("Deleted webservice " + serviceName);
			return Response.ok().header(ORIGIN, CLIENT).build();
		} catch (Exception e) {
			Logger.warning("Failed to delete service " + serviceName, e);
			return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Failed to remove the webservice").header(ORIGIN, CLIENT).build();
		}
	}
}
