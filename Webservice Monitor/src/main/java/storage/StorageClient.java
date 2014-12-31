package storage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.MetricSet;
import model.Webservice;
import model.WebserviceMetricUpdate;
import model.WebserviceSummary;
import model.dependency.LinkSeverity;
import model.dependency.WebserviceLink;
import util.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StorageClient {
	
	private static Database database;
	
	private final static String WEBSERVICE_TABLE = "webservice";
	private final static String DATA_TABLE = "webservice_metrics";
	private final static String WEBSERVICE_LINK = "webservice_link";
	private final static String WEBSERVICE_NAME = "name";
	private final static String WEBSERVICE_URL = "url";
	private final static String WEBSERVICE_DATE = "date";
	private final static String WEBSERVICE_RESPONSE_CODE = "status";
	private final static String WEBSERVICE_RESPONSE_TIME = "response_time";
	private final static String WEBSERVICE_LINK_1_NAME = "name_1";
	private final static String WEBSERVICE_LINK_2_NAME = "name_2";
	private final static String WEBSERVICE_LINK_1_URL = "url_1";
	private final static String WEBSERVICE_LINK_2_URL = "url_2";
	private final static String WEBSERVICE_LINK_SEVERITY = "severity";
	
	public StorageClient() {
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
	
	public List<WebserviceMetricUpdate> listWebservices() throws IOException, SQLException {
		PreparedStatement statement = database.getConnection().prepareStatement("SELECT t1.name, t1.url, t2.* FROM " + WEBSERVICE_TABLE + " t1 LEFT JOIN (SELECT name, max(date) as mx from " + DATA_TABLE + " GROUP BY name) as X ON X.name = t1.name LEFT JOIN " + DATA_TABLE + " t2 ON t2.name = X.name AND t2.date = X.mx");
		
		Logger.info("Making Query: [" + statement + "]");
		ResultSet result = statement.executeQuery();
		List<WebserviceMetricUpdate> services = Lists.newArrayList();
		while(result.next()){
			String webserviceName = result.getString(WEBSERVICE_NAME);
			String webserviceUrl = result.getString(WEBSERVICE_URL);
			int responseCode = result.getInt(WEBSERVICE_RESPONSE_CODE);
			long responseTime = result.getLong(WEBSERVICE_RESPONSE_TIME);
			Timestamp datetime = result.getTimestamp(WEBSERVICE_DATE);
			if(datetime != null){
				services.add(new WebserviceMetricUpdate(webserviceName, webserviceUrl, new MetricSet(responseCode, responseTime, datetime.getTime())));
			}else{
				services.add(new WebserviceMetricUpdate(webserviceName, webserviceUrl, null));
			}
			
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
	
	public void createWebserviceLink(WebserviceLink link) throws SQLException, IOException{
		PreparedStatement statement = database.getConnection().prepareStatement("REPLACE INTO " + WEBSERVICE_LINK + " VALUES (?, ?, ?)");
		statement.setString(1, link.getService1().getName());
		statement.setString(2, link.getService2().getName());
		statement.setInt(3, link.getSeverity().getIntValue());
		
		Logger.info("Making Query: [" + statement + "]");
		statement.executeUpdate();
	}
	
	public List<WebserviceLink> getLinks(String serviceName) throws SQLException, IOException {
		PreparedStatement statement = database.getConnection().prepareStatement("select * from (SELECT dt.name_1, dt.name_2, dt.direction, dt.severity, wt1.url AS url_1, wt2.url AS url_2 FROM " + WEBSERVICE_LINK + " dt  INNER JOIN " + WEBSERVICE_TABLE + " wt1 ON dt.name_1 = wt1.name INNER JOIN " + WEBSERVICE_TABLE + " wt2 ON dt.name_2 = wt2.name) w where (w.name_1 = ? OR w.name_2 = ?)");
		statement.setString(1, serviceName);
		statement.setString(2, serviceName);
		
		Logger.info("Making Query: [" + statement + "]");
		ResultSet result = statement.executeQuery();
		
		List<WebserviceLink> links = Lists.newArrayList();
		while(result.next()){
			Webservice service1 = new Webservice(result.getString(WEBSERVICE_LINK_1_NAME), result.getString(WEBSERVICE_LINK_1_URL));
			Webservice service2 = new Webservice(result.getString(WEBSERVICE_LINK_2_NAME), result.getString(WEBSERVICE_LINK_2_URL));
			LinkSeverity severity = LinkSeverity.valueOf(result.getString(WEBSERVICE_LINK_SEVERITY));
			
			links.add(new WebserviceLink(service1, service2, severity));
		}
		
		return links;
	}
	
	public List<WebserviceSummary> getOverview() throws SQLException, IOException {
		PreparedStatement statement = database.getConnection().prepareStatement("select w.name, w.url, m.status, m.response_time, m.date from webservice w join webservice_current_metrics m on w.name=m.name");
		
		Logger.info("Making Query: [" + statement + "]");
		ResultSet result = statement.executeQuery();
		Map<String, WebserviceSummary> entries = Maps.newHashMap();
		while(result.next()){
			String name = result.getString(WEBSERVICE_NAME);
			String url = result.getString(WEBSERVICE_URL);
			Webservice service = new Webservice(name, url);
			
			int status = result.getInt(WEBSERVICE_RESPONSE_CODE);;
			long response_time = result.getLong(WEBSERVICE_RESPONSE_TIME);
			Timestamp date = result.getTimestamp(WEBSERVICE_DATE);
			MetricSet metrics = new MetricSet(status,response_time, date.getTime());
			
			entries.put(name, new WebserviceSummary(service, metrics, Lists.newArrayList()));
		}
		
		statement = database.getConnection().prepareStatement("select * from webservice_link");
		Logger.info("Making Query: [" + statement + "]");
		result = statement.executeQuery();
		while(result.next()){
			String name1 = result.getString(WEBSERVICE_LINK_1_NAME);
			String name2 = result.getString(WEBSERVICE_LINK_2_NAME);
			WebserviceSummary summary = entries.get(name1);
			if(summary != null) {
				summary.getDependantSystems().add(name2);
			}
		}
		return new ArrayList<WebserviceSummary>(entries.values());
	}
}
