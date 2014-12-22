package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

	private final static String url = "";
	private final static String user = "";
	private final static String password = "";

	private static Connection connection;
	private static PreparedStatement statement;
	
	public void connect() throws SQLException {
		connection = DriverManager.getConnection(url, user, password);
		Logger.info("Connecting to " + url);
	}
	
	public void disconnect() throws SQLException {
		if(connection != null && !connection.isClosed()){
			connection.close();
		}
		Logger.info("Disconnecting from " + url);
	}
	
	public ResultSet executeQuery(String query, String...params) throws SQLException{
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
	
	public int executeUpdate(String query, String...params) throws SQLException{
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

	@Override
	protected void finalize() throws Throwable {
		disconnect();
		super.finalize();
	}
}
