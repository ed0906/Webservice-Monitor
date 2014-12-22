package util;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Webservice;

import com.google.common.collect.Lists;

public class WebserviceStorageManager {
	
	private static Database database;
	
	private final static String WEBSERVICE_NAME = "name";
	private final static String WEBSERVICE_URL = "url";
	
	public WebserviceStorageManager() {
		if(database == null){
			database = new Database();
		}
	}

	public void clear() throws SQLException {
		database.executeUpdate("DELETE FROM webservice");
	}
	
	public Webservice getWebservice(String serviceName) throws SQLException{
		ResultSet result = database.executeQuery("SELECT * FROM webservice WHERE "+ WEBSERVICE_NAME + " = ?", serviceName);
		if(result.next()){
			String webserviceName = result.getString(WEBSERVICE_NAME);
			String webserviceUrl = result.getString(WEBSERVICE_URL);
			return new Webservice(webserviceName, webserviceUrl);
		}
		return null;
	}

	public void save(Webservice service) throws SQLException {
		database.executeUpdate("REPLACE INTO webservice VALUES (?, ?)", service.getName(), service.getUrl());
	}
	
	public List<Webservice> listWebservices() throws IOException, SQLException {
		ResultSet result = database.executeQuery("SELECT * FROM webservice");
		List<Webservice> services = Lists.newArrayList();
		while(result.next()){
			String webserviceName = result.getString(WEBSERVICE_NAME);
			String webserviceUrl = result.getString(WEBSERVICE_URL);
			services.add(new Webservice(webserviceName, webserviceUrl));
		}
		return services;
	}

	public void delete(String serviceName) throws SQLException {
		database.executeUpdate("DELETE FROM webservice WHERE " + WEBSERVICE_NAME + " = ?", serviceName);
	}	
}
