package resource;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import runner.WebserviceRunner;

public class MainResourceTest {

	private static WebserviceRunner runner;
	
	@BeforeClass
	public static void setUp() throws Exception {
		runner = new WebserviceRunner("localhost",8135);
		runner.start();
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		runner.stop();
	}
	
	@Test
	public void testHealthCheck() throws IOException {
		Response response = get("/api/healthcheck");
		
		assertEquals(200, response.getStatus());
	}
	
	private static Response get(String endpoint) throws IOException {
		URL url = new URL("http://" + runner.getHostAndPort() + endpoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStreamReader reader = new InputStreamReader(connection.getInputStream());
		
		int status = connection.getResponseCode();
		
		StringBuilder builder = new StringBuilder();
		int component;
		while((component = reader.read()) != -1) {
			builder.append((char) component);
		}
		
		return Response.status(status).entity(builder.toString()).build();
	}
}
