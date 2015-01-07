package resource.dependency;

import java.net.HttpURLConnection;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import model.Webservice;
import model.dependency.LinkSeverity;
import model.dependency.WebserviceLink;

import org.apache.commons.lang3.StringUtils;

import resource.Resource;
import util.Logger;
import api.MonitorAPI;

@Path("api/service/dependency/add")
public class AddDependencyResource extends Resource {

	@POST
	@Produces(MEDIA_TYPE)
	public Response getAllDependencies(@QueryParam("parent") String parent, @QueryParam("child") String child) {
		Logger.info("Add Dependencies Request");

		MonitorAPI api = new MonitorAPI();
		try {
			if (StringUtils.isEmpty(parent) || StringUtils.isEmpty(child)) {
				Logger.warning("Add dependencies failed: Null input");
				return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).header(ORIGIN, CLIENT).build();
			}

			WebserviceLink link = new WebserviceLink(new Webservice(parent, null), new Webservice(child, null), LinkSeverity.STRONG);
			api.addWebserviceLink(link);
			return Response.ok().header(ORIGIN, CLIENT).build();
		} catch (Exception e) {
			Logger.warning("Add dependencies failed", e);
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).header(ORIGIN, CLIENT).build();
		}
	}

}
