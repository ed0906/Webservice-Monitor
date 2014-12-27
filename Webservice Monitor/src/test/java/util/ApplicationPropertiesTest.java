package util;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import storage.Database;

public class ApplicationPropertiesTest {
		
	@Test
	public void shouldLoadProperties() throws IOException {
		assertNotNull(ApplicationProperties.getValueOf(Database.PROPERTY_KEY_URL));
		assertNotNull(ApplicationProperties.getValueOf(Database.PROPERTY_KEY_USER));
		assertNotNull(ApplicationProperties.getValueOf(Database.PROPERTY_KEY_PASSWORD));
	}
}
