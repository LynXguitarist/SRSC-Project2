package accessControl;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;

import api.AccessControl;
import api.Auth;

public class AccessControlService implements AccessControl {

	private static final String PATH = "access.conf";

	private Properties prop;
	private static Client client;
	private String serverUrl;

	public AccessControlService(String serverUrl) {
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
	public String login(String username, String password) {
		boolean isActive = false;
		// chama o AuthService para verificar se um cliente existe ou nao(client call)
		WebTarget target = client.target(serverUrl).path(Auth.PATH);
		Response r = target.path(username).request().accept(MediaType.APPLICATION_JSON).get();

		if (r.getStatus() == Status.OK.getStatusCode()) {
			if (!r.hasEntity())
				System.out.println("The path is empty!");
			else {
				isActive = r.readEntity(new GenericType<Boolean>() {
				});
			}
		} else
			throw new WebApplicationException(Status.NOT_FOUND);

		if (isActive) {
			// verifica para aquele user as permissoes(getAccessPermissions)
			String permission = getAccessPermissions(username);
			String token = "";
			// gera token
			// guarda token em session para aquele user
			return token;
		}
		// has no permissions, isn't active, etc...
		throw new WebApplicationException(Status.FORBIDDEN);
	}

	@Override
	public String getAccessPermissions(String username) {
		String permission = prop.getProperty(username);
		return permission;
	}

}
