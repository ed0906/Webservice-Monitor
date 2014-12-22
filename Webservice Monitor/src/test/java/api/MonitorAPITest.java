package api;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import model.Webservice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MonitorAPITest {
	
	private MonitorAPI monitorApi;
	
	private final static String TEST_WEBSERVICE1_NAME = "name1";
	private final static String TEST_WEBSERVICE2_NAME = "name2";
	private final static String TEST_WEBSERVICE1_URL = "http://www.google.com";
	private final static String TEST_WEBSERVICE2_URL = "http://www.google.com";

	@Before
	public void setUp() throws SQLException {
		monitorApi = new MonitorAPI();
	}
	
	@Test
	public void shouldSaveAndGetNewWebservice() throws IOException, SQLException {
		Webservice service = new Webservice(TEST_WEBSERVICE1_NAME,TEST_WEBSERVICE1_URL);
		monitorApi.addWebservice(service);
		
		assertEquals(service, monitorApi.getWebservice(TEST_WEBSERVICE1_NAME));
	}
	
	@Test
	public void shouldGetListOfSavedWebservices() throws Exception {
		Webservice service1 = new Webservice(TEST_WEBSERVICE1_NAME,TEST_WEBSERVICE1_URL);
		monitorApi.addWebservice(service1);
		
		Webservice service2 = new Webservice(TEST_WEBSERVICE2_NAME,TEST_WEBSERVICE2_URL);
		monitorApi.addWebservice(service2);
		
		List<Webservice> services = monitorApi.getWebserviceList();
		assertTrue(services.contains(service1));
		assertTrue(services.contains(service2));
	}
		
	@Test
	public void shouldRequestUpdateWithStatus200() throws Exception {
		Webservice service = new Webservice(TEST_WEBSERVICE1_NAME,TEST_WEBSERVICE1_URL);
		monitorApi.addWebservice(service);
		
		Map<Metric, Number> metricMap = monitorApi.getUpdate(TEST_WEBSERVICE1_NAME, Metric.values());
		
		assertEquals(200, metricMap.get(Metric.RESPONSE_CODE).intValue());
		assertNotNull(metricMap.get(Metric.RESPONSE_TIME_NANOS).intValue());
	}
	
	@After
	public void tearDown() throws SQLException {
		monitorApi.deleteWebservice(TEST_WEBSERVICE1_NAME);
		monitorApi.deleteWebservice(TEST_WEBSERVICE2_NAME);
	}
}
