package api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import model.MetricSet;
import model.Webservice;
import model.WebserviceMetricUpdate;
import model.WebserviceSummary;
import storage.StorageClient;

public class MonitorAPI {
	
	private static StorageClient storage;
	
	public MonitorAPI() {
		if(storage == null) {
			storage = new StorageClient();
		}
	}
	
	public void reset() throws SQLException, IOException {
		storage.clear();
	}

	public void addWebservice(Webservice webservice) throws IOException, SQLException {
		storage.save(webservice);
	}

	public Webservice getWebservice(String serviceName) throws IOException, SQLException {
		return storage.getWebservice(serviceName);
	}
	
	public List<WebserviceMetricUpdate> getWebserviceList() throws SQLException, Exception {
		return storage.listWebservices();
	}
	
	public void deleteWebservice(String serviceName) throws SQLException, IOException {
		storage.delete(serviceName);
	}
	
	public MetricSet getUpdate(String serviceName) throws SQLException, Exception {
		Webservice service = getWebservice(serviceName);
		
		URL url = new URL(service.getUrl());
		long startTime = System.currentTimeMillis();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		int status = connection.getResponseCode();
		long elapsedTime = System.currentTimeMillis() - startTime;
		
		return new MetricSet(status, elapsedTime, new Date().getTime());
	}
	
	public MetricSet getMetrics(String serviceName) throws SQLException, Exception {
		return storage.getLatestMetrics(serviceName);
	}
	
	public List<MetricSet> getMetrics(String serviceName, Date from, Date until) throws SQLException, Exception {
		return storage.getMetricsBetween(serviceName, from.getTime(), until.getTime());
	}
	
	public List<WebserviceSummary> getWebserviceSummaries() throws SQLException, IOException {
		return storage.getOverview();
	}
}
