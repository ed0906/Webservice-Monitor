package resource;

import javax.ws.rs.core.MediaType;

public abstract class Resource {

	protected final static String MEDIA_TYPE = MediaType.APPLICATION_JSON;
	protected final static String ORIGIN = "Access-Control-Allow-Origin";
	protected final static String CLIENT = "*";
	
}
