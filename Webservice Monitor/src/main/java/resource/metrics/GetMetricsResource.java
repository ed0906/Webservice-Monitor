package resource.metrics;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import model.MetricSet;

import org.apache.commons.lang3.StringUtils;

import resource.Resource;
import util.Logger;
import api.MonitorAPI;

@Path("api/metrics")
public class GetMetricsResource extends Resource{

	@GET
	@Produces(MEDIA_TYPE)
	public Response getMetrics(@QueryParam("service-name") String serviceName, @QueryParam("from") String from, @QueryParam("until") String until){
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
	
}
