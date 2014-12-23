package api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.MetricSet;
import model.Webservice;
import model.WebserviceOverview;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.WebserviceStorageManager;

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
		WebserviceStorageManager storage = new WebserviceStorageManager();
		
		MetricSet metrics1 = new MetricSet(0,0,0);
		Webservice service1 = new WebserviceOverview(TEST_WEBSERVICE1_NAME,TEST_WEBSERVICE1_URL, metrics1);
		monitorApi.addWebservice(service1);
		storage.save(TEST_WEBSERVICE1_NAME, metrics1);
		
		MetricSet metrics2 = new MetricSet(0,0,0);
		Webservice service2 = new WebserviceOverview(TEST_WEBSERVICE2_NAME,TEST_WEBSERVICE2_URL, metrics2);
		monitorApi.addWebservice(service2);
		storage.save(TEST_WEBSERVICE2_NAME, new MetricSet(0,0,0));
		
		List<WebserviceOverview> services = monitorApi.getWebserviceList();
		assertTrue(services.contains(service1));
		assertTrue(services.contains(service2));
	}
		
	@Test
	public void shouldRequestUpdateWithStatus200() throws Exception {
		Webservice service = new Webservice(TEST_WEBSERVICE1_NAME,TEST_WEBSERVICE1_URL);
		monitorApi.addWebservice(service);
		
		MetricSet metrics = monitorApi.getUpdate(TEST_WEBSERVICE1_NAME);
		
		assertEquals(200, metrics.getResponseCode());
		assertNotNull(metrics.getResponseTime());
		assertNotNull(metrics.getDate());
	}
	
	@After
	public void tearDown() throws SQLException, IOException {
		monitorApi.deleteWebservice(TEST_WEBSERVICE1_NAME);
		monitorApi.deleteWebservice(TEST_WEBSERVICE2_NAME);
	}
}
