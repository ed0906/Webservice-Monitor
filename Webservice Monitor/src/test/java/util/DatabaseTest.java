package util;

import static org.junit.Assert.*;

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
	public void testSelect() {
		try {
			database.executeQuery("SELECT * FROM webservice");
		} catch (SQLException e) {
			fail();
		}
	}
}
