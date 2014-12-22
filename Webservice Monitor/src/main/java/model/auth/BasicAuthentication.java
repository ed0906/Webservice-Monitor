package model.auth;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.google.common.collect.Maps;

public class BasicAuthentication implements Authentication {

	private String encodedAuth;
	
	public BasicAuthentication(String username, String password){
		encodedAuth = new Base64().encodeAsString((username + ":" + password).getBytes());
	}

	@Override
	public Map<String, String> getAuthenticationHeaders() {
		Map<String, String> headers = Maps.newHashMap();
		headers.put("Authorization", "Basic " + encodedAuth);
		return headers;
	}
}
