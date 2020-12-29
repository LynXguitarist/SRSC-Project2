package frontend;

import java.io.FileInputStream;
import java.io.FileReader;
import java.security.KeyStore;
import java.util.Properties;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class TLS_SERVER {

	private static final String TLS_FILE = "tls.conf";
	//private static final String TRUST_FILE = "trustedserverstore";
	private static final String KEY_FILE = "server.jks";

	private static Properties prop;

	public static SSLContext getSSLContext() throws Exception {
		/*
		 * Set up a key manager for client authentication if asked by the server. Use
		 * the implementation's default TrustStore and secureRandom routines.
		 */
		SSLSocketFactory factory = null;

		prop = new Properties();
		FileReader file = new FileReader(TLS_FILE);
		prop.load(file);

		String[] confciphersuites = prop.getProperty("CIPHERSUITES").split(",");

		char[] passphrase = "client".toCharArray();

		String tls = "";
		if (prop.getProperty("TLS-PROT-ENF").equals("TLS-1.1"))
			tls = "TLSv1.1";
		else
			tls = "TLSv1.2";

		SSLContext ctx = SSLContext.getInstance(tls);

		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		KeyStore ks = KeyStore.getInstance("JKS");

		ks.load(new FileInputStream(KEY_FILE), passphrase);

		kmf.init(ks, passphrase);
		ctx.init(kmf.getKeyManagers(), null, null);

		factory = ctx.getSocketFactory();

		SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
		SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(8080);

		// s.setEnabledProtocols(confprotocols);
		s.setEnabledCipherSuites(confciphersuites);

		return ctx;
	}

}
