package auth;

import java.util.Hashtable;
import java.util.Map;

import api.Auth;
import utils.UserInfo;

public class AuthService implements Auth {

	private Map<String, UserInfo> authTable;

	public AuthService() {
		this.authTable = new Hashtable<>();
	}

	@Override
	public boolean isActive(String username) {
		if (!authTable.containsKey(username))
			return false;
		return authTable.get(username).isActive();
	}
}
