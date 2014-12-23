package api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import model.Metric;
import model.Webservice;
import util.WebserviceStorageManager;

import com.google.common.collect.Maps;

public class MonitorAPI {
	
	private static WebserviceStorageManager storage;
	
	public MonitorAPI() {
		if(storage == null) {
			storage = new WebserviceStorageManager();
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
	
	public List<Webservice> getWebserviceList() throws SQLException, Exception {
		return storage.listWebservices();
	}
	
	public void deleteWebservice(String serviceName) throws SQLException, IOException {
		storage.delete(serviceName);
	}
	
	public Map<Metric, Number> getUpdate(String serviceName, Metric...metrics) throws SQLException, Exception {
		Webservice service = getWebservice(serviceName);
		
		URL url = new URL(service.getUrl());
		long startTime = System.nanoTime();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		int status = connection.getResponseCode();
		long elapsedTime = System.nanoTime() - startTime;
		
		Map<Metric, Number> metricMap = Maps.newHashMap();
		for(Metric metric : metrics) {
			switch(metric) {
			case RESPONSE_CODE:
				metricMap.put(Metric.RESPONSE_CODE, status);
				break;
			case RESPONSE_TIME_NANOS:
				metricMap.put(Metric.RESPONSE_TIME_NANOS, elapsedTime);
				break;
			}
		}
		return metricMap;
	}
}
