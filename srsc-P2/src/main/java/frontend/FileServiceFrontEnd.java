package frontend;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import accessControl.AccessControlService;
import auth.AuthService;
import fileStorage.FileStorageService;
import utils.InsecureHostnameVerifier;

public class FileServiceFrontEnd {

	// Server for the services(Main dispatcher)
	// Sub-resources -> Auth, access, storage
	// Called by client
	// Define which service will be implemented and where(Login, ls, etc)

	private static Logger Log = Logger.getLogger(FileServiceFrontEnd.class.getName());

	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
	}

	public static final int PORT = 8080;

	public static void main(String[] args) throws UnknownHostException {
		InetAddress localHost = InetAddress.getLocalHost();
		String ip = localHost.getHostAddress();

		URI serverURI = URI.create(String.format("https://%s:%s/rest", ip, PORT));

		HttpsURLConnection.setDefaultHostnameVerifier(new InsecureHostnameVerifier());

		ResourceConfig config = new ResourceConfig();
		config.register(new AccessControlService(serverURI.toString()));
		config.register(new AuthService(serverURI.toString()));
		config.register(new FileStorageService(serverURI.toString()));

		// usar TLS feito como na aula
		try {
			// SSLContext.getDefault()
			JdkHttpServerFactory.createHttpServer(serverURI, config, TLS_SERVER.getSSLContext());
		} catch (Exception e) {
			System.err.println("Invalid SSL/TLS configuration.");
			e.printStackTrace();
			System.exit(1);
		}
		Log.info(String.format("REST Server ready @ %s\n", serverURI));
	}
	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean isActive() {
		return true;
	}

}
