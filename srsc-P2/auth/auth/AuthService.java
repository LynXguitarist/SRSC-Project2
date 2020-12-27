package auth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import api.Auth;
import utils.AServer;
import utils.KToken;
import utils.PublicNumDH;
import utils.ResponsePNDH;
import utils.UserInfo;

public class AuthService implements Auth {

	private static final int TTL = 300000; // 5 min
	private static final String AUTH_FILE = "authTable.conf";
	// Username, UserInfo
	private Map<String, UserInfo> authTable;

	public AuthService() {
		this.authTable = new Hashtable<>();
	}
	// add e update talvez nao precisam de ser servicos
	// ter so um -> createOrUpdate

	@Override
	public void addUser(UserInfo userInfo) {
		authTable.put(userInfo.getUsername(), userInfo);
	}

	@Override
	public void updateUser(String username, UserInfo userInfo) {
		authTable.put(username, userInfo);
	}

	@Override
	public boolean isActive(String username) throws IOException {
		if (!authTable.containsKey(username) && !hasUser(username))
			throw new WebApplicationException(Status.NOT_FOUND);

		return authTable.get(username).isActive();
	}

	@Override
	public void deleteUser(String username) {
		authTable.remove(username);
	}

	private boolean hasUser(String username) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(AUTH_FILE));
		String line = "";
		while ((line = reader.readLine()) != null) {
			String[] userInfo = line.split(":");
			if (username.equals(userInfo[0]) || username.equals(userInfo[1])) {
				addUserInfo(userInfo);
				reader.close();
				return true;
			}
		}
		reader.close();
		return false;
	}

	private void addUserInfo(String[] userInfo) {
		String username = userInfo[0];
		UserInfo user = new UserInfo(username, userInfo[1], userInfo[2], userInfo[3],
				Boolean.parseBoolean(userInfo[4]));
		authTable.put(username, user);
	}
	
	//---------------------------------DH-------------------------------------//

	@Override
	public PublicNumDH startDH(String username) {
		// verify user -> username
		
		SecureRandom random = new SecureRandom();
		// Yserver vem do DH
		String Yaserver = "";
		
		PublicNumDH pub = new PublicNumDH(random, Yaserver);
		
		return pub;
	}

	@Override
	public AServer lastAggreement(ResponsePNDH response) {
		String A ="";
		String kToken1024 = "";
		long ttl = System.currentTimeMillis() + TTL;
		String other = "";
		
		KToken kToken = new KToken(A, kToken1024, ttl, other);
		int random2 = response.getRandom2().nextInt();
		
		AServer aServer = new AServer(kToken, random2);
		
		return aServer;
	}

}
