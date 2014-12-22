package api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import util.Logger;
import model.Webservice;

public class WebserviceValidator {

	public static int getResponseCode(Webservice service) {
		try {
			URL url = new URL(service.getUrl());
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			return connection.getResponseCode();
		} catch (IOException e) {
			Logger.warning("Webservice validation failed", e);
			return HttpURLConnection.HTTP_BAD_REQUEST;
		}
	}
}
