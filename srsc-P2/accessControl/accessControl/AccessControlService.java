package accessControl;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import api.AccessControl;
import utils.AServer;
import utils.UserToken;

public class AccessControlService implements AccessControl {

	private static final String PATH = "access.conf";

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
	public Response login(String username, AServer aServer) {
		String token = aServer.getkToken().getkToken1024();
		long ttl = aServer.getkToken().getTtl();

		tokens.put(username, new UserToken(token, ttl));

		Response response = Response.status(Response.Status.OK).header("Authorization", token).build();

		return response;
	}

	@Override
	public boolean isTokenValid(String username) {
		UserToken token = tokens.get(username);

		if (token != null) {
			long ttl = token.getTtl();
			// if ttl > currentTime in miliseconds
			return ttl - System.currentTimeMillis() > 0;
		}
		
		return false;
	}

	@Override
	public String getAccessPermissions(String username) {
		// since token isn't valid, permission will be = "deny"
		if(isTokenValid(username))
			return "deny";
		
		String permission = prop.getProperty(username);
		return permission;
	}

}
