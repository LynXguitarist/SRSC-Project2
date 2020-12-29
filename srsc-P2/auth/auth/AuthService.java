package auth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import api.Auth;
import utils.AServer;
import utils.DHServer;
import utils.KToken;
import utils.PublicNumDH;
import utils.ResponsePNDH;
import utils.UserInfo;

public class AuthService implements Auth {

	private static final int TTL = 300000; // 5 min
	private static final String AUTH_FILE = "authTable.conf";

	private String serverUrl;

	// Username, UserInfo
	private Map<String, UserInfo> authTable;

	public AuthService(String serverUrl) {
		this.authTable = new Hashtable<>();
		this.serverUrl = serverUrl;
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

	// ---------------------------------DH-------------------------------------//

	@Override
	public PublicNumDH startDH(String username) throws Exception {
		if (!hasUser(username))
			throw new WebApplicationException("User " + username + " doesn't exist", Status.NOT_FOUND);
		// checks users permission --TODO

		SecureRandom random = new SecureRandom();
		// Yserver vem do DH
		DHServer dhS = new DHServer();
		KeyPair Yaserver = dhS.init();

		PublicNumDH pub = new PublicNumDH(random, Yaserver);

		return pub;
	}

	@Override
	public AServer lastAggreement(ResponsePNDH response) {
		// A -> the one to be trusted
		String A = serverUrl; // ou ir buscar ao certificado
		String kToken1024 = generateToken();
		long ttl = System.currentTimeMillis() + TTL;

		KToken kToken = new KToken(A, kToken1024, ttl);
		int random2 = response.getRandom2().nextInt();

		AServer aServer = new AServer(kToken, random2);

		return aServer;
	}

	// -------------------------------Private_Methods-------------------------------------//

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
	
	private String generateToken() {
		byte[] randomBytes = new byte[24];
		
		SecureRandom secureRandom = new SecureRandom();
	    secureRandom.nextBytes(randomBytes);
	    
	    Base64.Encoder base64Encoder = Base64.getUrlEncoder();
	    return base64Encoder.encodeToString(randomBytes);
	}

}
