package auth;

import java.util.Hashtable;
import java.util.Map;

import api.Auth;
import utils.UserInfo;

public class AuthService implements Auth {

	// Username, UserInfo
	private Map<String, UserInfo> authTable;

	public AuthService() {
		this.authTable = new Hashtable<>();
	}

	@Override
	public void addUser(UserInfo userInfo) {
		authTable.put(userInfo.getUsername(), userInfo);
	}

	@Override
	public void updateUser(String username, UserInfo userInfo) {
		authTable.put(username, userInfo);
	}

	@Override
	public boolean isActive(String username) {
		if (!authTable.containsKey(username))
			return false;
		return authTable.get(username).isActive();
	}

}
