package storage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import util.Logger;
import model.MetricSet;
import model.Webservice;
import model.WebserviceUpdate;

import com.google.common.collect.Lists;

public class WebserviceStorageManager {
	
	private static Database database;
	
	private final static String WEBSERVICE_TABLE = "webservice";
	private final static String DATA_TABLE = "webservice_metrics";
	private final static String WEBSERVICE_NAME = "name";
	private final static String WEBSERVICE_URL = "url";
	private final static String WEBSERVICE_DATE = "date";
	private final static String WEBSERVICE_RESPONSE_CODE = "status";
	private final static String WEBSERVICE_RESPONSE_TIME = "response_time";
	
	public WebserviceStorageManager() {
		if(database == null){
			database = new Database();
		}
	}

	public void clear() throws SQLException, IOException {
		PreparedStatement statement1 = database.getConnection().prepareStatement("DELETE FROM " + WEBSERVICE_TABLE);
		
		Logger.info("Making Query: [" + statement1 + "]");
		statement1.executeUpdate();
		
		PreparedStatement statement2 = database.getConnection().prepareStatement("DELETE FROM " + DATA_TABLE);
		
		Logger.info("Making Query: [" + statement2 + "]");
		statement2.executeUpdate();
	}
	
	public Webservice getWebservice(String serviceName) throws SQLException, IOException{
		PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM " + WEBSERVICE_TABLE + " WHERE "+ WEBSERVICE_NAME + " = ?");
		statement.setString(1, serviceName);
		
		Logger.info("Making Query: [" + statement + "]");
		ResultSet result = statement.executeQuery();
		if(result.next()){
			String webserviceName = result.getString(WEBSERVICE_NAME);
			String webserviceUrl = result.getString(WEBSERVICE_URL);
			return new Webservice(webserviceName, webserviceUrl);
		}
		return null;
	}

	public void save(Webservice service) throws SQLException, IOException {
		PreparedStatement statement = database.getConnection().prepareStatement("REPLACE INTO " + WEBSERVICE_TABLE + " VALUES (?, ?)");
		statement.setString(1, service.getName());
		statement.setString(2, service.getUrl());
		
		Logger.info("Making Query: [" + statement + "]");
		statement.executeUpdate();
	}
	
	public List<WebserviceUpdate> listWebservices() throws IOException, SQLException {
		PreparedStatement statement = database.getConnection().prepareStatement("SELECT t1.name, t1.url, t2.* FROM " + WEBSERVICE_TABLE + " t1 LEFT JOIN (SELECT name, max(date) as mx from " + DATA_TABLE + " GROUP BY name) as X ON X.name = t1.name LEFT JOIN " + DATA_TABLE + " t2 ON t2.name = X.name AND t2.date = X.mx");
		
		Logger.info("Making Query: [" + statement + "]");
		ResultSet result = statement.executeQuery();
		List<WebserviceUpdate> services = Lists.newArrayList();
		while(result.next()){
			String webserviceName = result.getString(WEBSERVICE_NAME);
			String webserviceUrl = result.getString(WEBSERVICE_URL);
			int responseCode = result.getInt(WEBSERVICE_RESPONSE_CODE);
			long responseTime = result.getLong(WEBSERVICE_RESPONSE_TIME);
			Timestamp datetime = result.getTimestamp(WEBSERVICE_DATE);
			services.add(new WebserviceUpdate(webserviceName, webserviceUrl, new MetricSet(responseCode, responseTime, datetime.getTime())));
		}
		return services;
	}

	public void delete(String serviceName) throws SQLException, IOException {
		PreparedStatement statement1 = database.getConnection().prepareStatement("DELETE FROM " + WEBSERVICE_TABLE + " WHERE " + WEBSERVICE_NAME + " = ?");
		statement1.setString(1, serviceName);
		
		Logger.info("Making Query: [" + statement1 + "]");
		statement1.executeUpdate();
		
		PreparedStatement statement2 = database.getConnection().prepareStatement("DELETE FROM " + DATA_TABLE + " WHERE " + WEBSERVICE_NAME + " = ?");
		statement2.setString(1, serviceName);
		
		Logger.info("Making Query: [" + statement2 + "]");
		statement2.executeUpdate();
	}
	
	public void save(String serviceName, MetricSet metrics) throws SQLException, IOException{
		PreparedStatement statement = database.getConnection().prepareStatement("REPLACE INTO " + DATA_TABLE + " VALUES (?, ?, ?, ?)");
		statement.setString(1, serviceName);
		statement.setInt(2, metrics.getResponseCode());
		statement.setLong(3, metrics.getResponseTime());
		statement.setTimestamp(4, new Timestamp(metrics.getDate()));
		
		Logger.info("Making Query: [" + statement + "]");
		statement.executeUpdate();
	}
	
	public MetricSet getLatestMetrics(String serviceName) throws SQLException, IOException{
		PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM " + DATA_TABLE + " WHERE " + WEBSERVICE_NAME + " = ? ORDER BY " + WEBSERVICE_DATE + " ASC LIMIT 1");
		statement.setString(1, serviceName);
		
		Logger.info("Making Query: [" + statement + "]");
		ResultSet result = statement.executeQuery();
		if(result.next()){
			int responseCode = result.getInt(WEBSERVICE_RESPONSE_CODE);
			long responseTime = result.getLong(WEBSERVICE_RESPONSE_TIME);
			Timestamp datetime = result.getTimestamp(WEBSERVICE_DATE);
			return new MetricSet(responseCode, responseTime, datetime.getTime());
		}
		return null;
	}
	
	public List<MetricSet> getMetricsBetween(String serviceName, long time1, long time2) throws SQLException, IOException{
		PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM " + DATA_TABLE + " WHERE (" + WEBSERVICE_NAME + " = ? AND " + WEBSERVICE_DATE + " BETWEEN ? AND ?)");
		statement.setString(1, serviceName);
		statement.setTimestamp(2, new Timestamp(time1));
		statement.setTimestamp(3, new Timestamp(time2));
		
		Logger.info("Making Query: [" + statement + "]");
		ResultSet result = statement.executeQuery();
		List<MetricSet> metrics = Lists.newArrayList();
		while(result.next()){
			int responseCode = result.getInt(WEBSERVICE_RESPONSE_CODE);
			long responseTime = result.getLong(WEBSERVICE_RESPONSE_TIME);
			Timestamp datetime = result.getTimestamp(WEBSERVICE_DATE);
			metrics.add(new MetricSet(responseCode, responseTime, datetime.getTime()));
		}
		return metrics;
	}
	
	public void deleteMetricsOlderThan(int days) throws SQLException, IOException{
		Connection connection = database.getConnection();
		
		PreparedStatement statement = connection.prepareStatement("SET SQL_SAFE_UPDATES=0");
		
		statement = connection.prepareStatement("DELETE FROM " + DATA_TABLE + " WHERE " + WEBSERVICE_DATE + " < (NOW() - INTERVAL ? DAY)");
		statement.setInt(1, days);
		
		Logger.info("Making Query: [" + statement + "]");
		statement.executeUpdate();
		
		statement = connection.prepareStatement("SET SQL_SAFE_UPDATES=1");
		statement.executeUpdate();
	}
}
