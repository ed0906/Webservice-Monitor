package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
	
	public final static String PROPERTY_KEY_URL = "database.url";
	public final static String PROPERTY_KEY_USER = "database.user";
	public final static String PROPERTY_KEY_PASSWORD = "database.password";

	private static Connection connection;
	private static PreparedStatement statement;
	
	public void connect() throws SQLException, IOException {
		String url = ApplicationProperties.getValueOf(PROPERTY_KEY_URL);
		String user = ApplicationProperties.getValueOf(PROPERTY_KEY_USER);
		String password = ApplicationProperties.getValueOf(PROPERTY_KEY_PASSWORD);
		
		connection = DriverManager.getConnection(url, user, password);
		Logger.info("Connecting to " + url);
	}
	
	public void disconnect() throws SQLException {
		if(connection != null && !connection.isClosed()){
			connection.close();
		}
		Logger.info("Disconnecting from database");
	}
	
	public ResultSet executeQuery(String query, String...params) throws SQLException, IOException{
		if(connection == null || connection.isClosed()){
			connect();
		}
		
		Logger.info("Executing update query: [" + String.format(query.replace("?", "'%s'"), params) + "]");
		statement = connection.prepareStatement(query);
		for(int i=0; i<params.length; i++){
			statement.setString(i+1, params[i]);
		}
		
		return statement.executeQuery();
	}
	
	public int executeUpdate(String query, String...params) throws SQLException, IOException{
		if(connection == null || connection.isClosed()){
			connect();
		}
		
		Logger.info("Executing update query: [" + String.format(query.replace("?", "'%s'"), params) + "]");
		statement = connection.prepareStatement(query);
		for(int i=0; i<params.length; i++){
			statement.setString(i+1, params[i]);
		}
		
		return statement.executeUpdate();
	}
	
	public boolean isConnected() throws SQLException {
		return !connection.isClosed();
	}

	@Override
	protected void finalize() throws Throwable {
		disconnect();
		super.finalize();
	}
}
