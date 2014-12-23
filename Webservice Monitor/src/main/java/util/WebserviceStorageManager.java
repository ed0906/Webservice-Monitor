package util;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.MetricSet;
import model.Webservice;

import com.google.common.collect.Lists;

public class WebserviceStorageManager {
	
	private Database database;
	
	private final static String WEBSERVICE_TABLE = "webservice";
	private final static String DATA_TABLE = "webservice_metrics";
	private final static String WEBSERVICE_NAME = "name";
	private final static String WEBSERVICE_URL = "url";
	private final static String WEBSERVICE_DATE = "date";
	private final static String WEBSERVICE_RESPONSE_CODE = "status";
	private final static String WEBSERVICE_RESPONSE_TIME = "response_time";
	
	public WebserviceStorageManager() {
		database = new Database();
	}

	public void clear() throws SQLException, IOException {
		PreparedStatement statement = database.getConnection().prepareStatement("DELETE FROM webservice");
		statement.executeUpdate();
	}
	
	public Webservice getWebservice(String serviceName) throws SQLException, IOException{
		PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM " + WEBSERVICE_TABLE + " WHERE "+ WEBSERVICE_NAME + " = ?");
		statement.setString(0, serviceName);
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
		statement.setString(0, service.getName());
		statement.setString(0, service.getUrl());
		statement.executeUpdate();
	}
	
	public List<Webservice> listWebservices() throws IOException, SQLException {
		PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM " + WEBSERVICE_TABLE);
		ResultSet result = statement.executeQuery();
		List<Webservice> services = Lists.newArrayList();
		while(result.next()){
			String webserviceName = result.getString(WEBSERVICE_NAME);
			String webserviceUrl = result.getString(WEBSERVICE_URL);
			services.add(new Webservice(webserviceName, webserviceUrl));
		}
		return services;
	}

	public void delete(String serviceName) throws SQLException, IOException {
		PreparedStatement statement = database.getConnection().prepareStatement("DELETE FROM " + WEBSERVICE_TABLE + " WHERE " + WEBSERVICE_NAME + " = ?");
		statement.setString(0, serviceName);
		statement.executeUpdate();
	}
	
	public void save(String serviceName, MetricSet metrics) throws SQLException, IOException{
		PreparedStatement statement = database.getConnection().prepareStatement("REPLACE INTO " + DATA_TABLE + " VALUES (?, ?, ?, ?)");
		statement.setString(0, serviceName);
		statement.setInt(1, metrics.getResponseCode());
		statement.setLong(2, metrics.getResponseTime());
		statement.setDate(0, new Date(metrics.getDate()));
		statement.executeUpdate();
	}
	
	public MetricSet getLatestMetrics(String serviceName) throws SQLException, IOException{
		PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM " + DATA_TABLE + " WHERE " + WEBSERVICE_NAME + " = ? ORDER BY " + WEBSERVICE_DATE + " ASC LIMIT 1");
		statement.setString(0, serviceName);
		ResultSet result = statement.executeQuery();
		if(result.next()){
			int responseCode = result.getInt(WEBSERVICE_RESPONSE_CODE);
			long responseTime = result.getLong(WEBSERVICE_RESPONSE_TIME);
			Date date = result.getDate(WEBSERVICE_DATE);
			return new MetricSet(responseCode, responseTime, date.getTime());
		}
		return null;
	}
}
