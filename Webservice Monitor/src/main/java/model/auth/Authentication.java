package model.auth;

import java.util.Map;

public interface Authentication {

	public Map<String,String> getAuthenticationHeaders();
}
