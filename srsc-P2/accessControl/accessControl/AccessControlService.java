package accessControl;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import api.AccessControl;
import utils.AServer;
import utils.UserToken;
import utils.Utils;

public class AccessControlService implements AccessControl {

	private static final String PATH = "access.conf";
	private static final int TTL = 300000; // 5 min

	// username, token
	private Map<String, UserToken> tokens;

	private Properties prop;
	private static Client client;
	private String serverUrl;

	public AccessControlService(String serverUrl) {
		this.tokens = new HashMap<>();

		this.serverUrl = serverUrl;
		ClientConfig config = new ClientConfig();
		client = ClientBuilder.newClient(config);

		init();
	}

	private void init() {
		prop = new Properties();
		FileReader file;
		try {
			file = new FileReader(PATH);
			prop.load(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Response login(String username, byte[] aServerbytes) throws ClassNotFoundException, IOException {
		AServer aServer = (AServer) Utils.convertFromBytes(aServerbytes);
		String token = aServer.getkToken().getkToken1024();
		long ttl = aServer.getkToken().getTtl();

		tokens.put(username, new UserToken(token, ttl));

		Response response = Response.status(Response.Status.OK).header("Authorization", token).build();

		return response;
	}

	private boolean isTokenValid(String username, String tokenFC) {
		UserToken token = tokens.get(username);

		if (token != null) {
			if (!token.getToken().equals(tokenFC))
				return false;

			long ttl = token.getTtl();
			// if ttl > currentTime in miliseconds
			if (ttl - System.currentTimeMillis() > 0) {
				// updates ttl
				tokens.put(username, new UserToken(tokenFC, ttl + TTL));
				return true;
			}
		}
		return false;
	}

	@Override
	public String getAccessPermissions(String username, HttpHeaders headers) {
		// token from client
		String tokenFC = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);

		// since token isn't valid, permission will be = "deny"
		if (!isTokenValid(username, tokenFC))
			return "deny";

		String permission = prop.getProperty(username);
		return permission;
	}

}
