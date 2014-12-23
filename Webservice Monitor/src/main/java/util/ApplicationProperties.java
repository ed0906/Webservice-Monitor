package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

	private static final String PROPERTIES_PATH = "monitor.properties";
	
	private static Properties properties;
	
	private static void loadProperties() throws IOException{
		properties = new Properties();
		
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_PATH);
		try {
			if(stream != null){
				properties.load(stream);
				stream.close();
			}else{
				throw new IOException();
			}
		} catch (IOException e) {
			Logger.error("No properties file found", e);
			throw e;
		}
	}
	
	public static String getValueOf(String propertyKey) throws IOException{
		if(properties == null){
			loadProperties();
		}
		
		return properties.getProperty(propertyKey);
	}
}
