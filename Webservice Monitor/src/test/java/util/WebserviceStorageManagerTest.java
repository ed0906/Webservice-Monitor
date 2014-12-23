package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.SQLException;

import model.Webservice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WebserviceStorageManagerTest {

	private WebserviceStorageManager storage;
	
	private final static String TEST_WEBSERVICE_NAME = "name";
	private final static String TEST_WEBSERVICE_URL = "url";
	
	@Before
	public void setUp() throws SQLException {
		storage = new WebserviceStorageManager();
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
	
	@After
	public void tearDown() throws SQLException, IOException {
		storage.delete(TEST_WEBSERVICE_NAME);
	}
}
