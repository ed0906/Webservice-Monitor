package util;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

public class DatabaseTest {

	private static Database database;
	
	@BeforeClass
	public static void setUp() {
		database = new Database();
	}
	
	@Test
	public void shouldConnect() throws SQLException, IOException {
		database.connect();
		
		assertTrue(database.isConnected());
	}
}
