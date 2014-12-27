package storage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import util.ApplicationProperties;
import util.Logger;

public class Database {
	
	public final static String PROPERTY_KEY_URL = "database.url";
	public final static String PROPERTY_KEY_USER = "database.user";
	public final static String PROPERTY_KEY_PASSWORD = "database.password";

	private static Connection connection;
	
	public boolean connect() throws SQLException, IOException {
		String url = ApplicationProperties.getValueOf(PROPERTY_KEY_URL);
		String user = ApplicationProperties.getValueOf(PROPERTY_KEY_USER);
		String password = ApplicationProperties.getValueOf(PROPERTY_KEY_PASSWORD);
		
		connection = DriverManager.getConnection(url, user, password);
		Logger.info("Connecting to " + url);
		
		boolean isConnected = isConnected();
		if(isConnected){
			Logger.info("Connection successful");
		}else{
			Logger.error("Connection failed");
		}
		return isConnected;
	}
	
	public void disconnect() throws SQLException {
		if(connection != null && !connection.isClosed()){
			connection.close();
		}
		Logger.info("Disconnecting from database");
	}
		
	public boolean isConnected() throws SQLException {
		return !connection.isClosed();
	}

	public Connection getConnection() throws SQLException, IOException {
		if(connection == null){
			connect();
		}
		
		return connection;
	}
	
	@Override
	protected void finalize() throws Throwable {
		disconnect();
		super.finalize();
	}
}
