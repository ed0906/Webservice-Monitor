package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import model.MetricSet;
import model.Webservice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import storage.StorageClient;

public class WebserviceStorageManagerTest {

	private StorageClient storage;
	
	private final static String TEST_WEBSERVICE_NAME = "name";
	private final static String TEST_WEBSERVICE_URL = "url";
	
	@Before
	public void setUp() throws SQLException {
		storage = new StorageClient();
	}
	
	@Test
	public void shouldSaveWebservice() throws IOException, SQLException {
		Webservice service = new Webservice(TEST_WEBSERVICE_NAME,TEST_WEBSERVICE_URL);
		
		storage.save(service);
		
		assertEquals(service, storage.getWebservice(TEST_WEBSERVICE_NAME));
	}
	
	@Test
	public void shouldDeleteWebservice() throws SQLException, IOException{
		Webservice service = new Webservice(TEST_WEBSERVICE_NAME,TEST_WEBSERVICE_URL);
		
		storage.save(service);
		storage.delete(TEST_WEBSERVICE_NAME);
		
		assertNull(storage.getWebservice(TEST_WEBSERVICE_NAME));
	}
	
	@Test
	public void shouldUpdateMetrics() throws SQLException, IOException {
		MetricSet metrics = new MetricSet(200, 0l, new Date(114, 11, 23, 18, 25, 15).getTime());
		
		storage.save(TEST_WEBSERVICE_NAME, metrics);
		
		assertEquals(metrics, storage.getLatestMetrics(TEST_WEBSERVICE_NAME));
	}
	
	@After
	public void tearDown() throws SQLException, IOException {
		storage.delete(TEST_WEBSERVICE_NAME);
	}
}
