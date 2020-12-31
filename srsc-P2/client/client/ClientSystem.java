package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;

import api.AccessControl;
import api.Auth;
import api.FileStorage;
import utils.AServer;
import utils.DHClient;
import utils.FilesToCopy;
import utils.Password;
import utils.PublicNumDH;
import utils.ResponsePNDH;
import utils.Utils;

//Calls the services
public class ClientSystem {

	private static final String SERVER_URL = "https://localhost:8080/rest/";

	private static Client client;
	private static String token;

	public static void main(String[] args) throws Exception {

		// ClientConfig config = new ClientConfig();
		// client = ClientBuilder.newClient(config);

		client = ClientBuilder.newBuilder().sslContext(TLS_CLIENT.getSSLContext()).build();
		token = ""; // to prevent NullPointer

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String line = in.readLine();
            String operation = line.split(" ")[0];
            // rest of the line -> after the operation + username
            String controls = line.substring(operation.length() + 1);

            switch (operation) {
            case "Login":
                operationLogin(controls);
                break;
            case "ls":
                operationLs(controls);
                break;
            case "mkdir":
                operationMkdir(controls);
                break;
            case "put":
                operationPut(controls);
                break;
            case "get":
                operationGet(controls);
                break;
            case "cp":
                operationCp(controls);
                break;
            case "rm":
                operationRm(controls);
                break;
            case "rmdir":
                operationRmdir(controls);
                break;
            case "file":
                operationFile(controls);
                break;
            case "test":
                test();
                break;
            default:
                return;
            }
		}
	}

	private static void operationLogin(String controls) throws Exception {
		String username = controls.split(" ")[0];
		String password = controls.split(" ")[1];

		AServer aServer = DHCall(username, password);
		if (aServer != null) {
			token = "";

			// calls login in accessControl
			WebTarget target = client.target(SERVER_URL).path(AccessControl.PATH);

			Response r = target.path(username).request().accept(MediaType.APPLICATION_OCTET_STREAM)
					.post(Entity.entity(Utils.convertToBytes(aServer), MediaType.APPLICATION_OCTET_STREAM));

			if (r.getStatus() == Status.OK.getStatusCode()) {
				token = r.getHeaderString("Authorization");
				System.out.println("User logged in and token: " + token);
				operationCreateUserFolder(username);
			}else
				System.out.println(r.getStatus() + " - user doesn't have access!");
		}
	}

	private static void operationLs(String controls) {
        String[] split = controls.split(" ");
        String username = split[0];
        String path = "";
        if (split.length == 2) {
            path = split[1];
        }

        WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);
        Response r = null;
        if (path != "")
            r = target.path(username).path(path).request().header(HttpHeaders.AUTHORIZATION, token)
                    .accept(MediaType.APPLICATION_JSON).get();
        else
            r = target.path(username).request().header(HttpHeaders.AUTHORIZATION, token)
                    .accept(MediaType.APPLICATION_JSON).get();

        if (r.getStatus() == Status.OK.getStatusCode()) {
            if (!r.hasEntity())
                System.out.println("The path is empty!");
            else {
                List<String> listOfFiles = r.readEntity(new GenericType<List<String>>() {
                });
                for (String file : listOfFiles) {
                    System.out.println(file);
                }
            }
        } else
            System.out.println(r.getStatus() + " - error while listing dirs and files!");
    }

	private static void operationMkdir(String controls) {
		String username = controls.split(" ")[0];
		String path = controls.split(" ")[1];

		WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);

		Response r = target.path(username).request().header(HttpHeaders.AUTHORIZATION, token)
				.accept(MediaType.APPLICATION_JSON).post(Entity.entity(path, MediaType.APPLICATION_JSON));

		if (r.getStatus() == Status.OK.getStatusCode())
			System.out.println("Path " + path + " created successfully!");
		else
			System.out.println(r.getStatus() + " - error creating path " + path);

	}

	private static void operationPut(String controls) {
		String username = controls.split(" ")[0];
		String[] path_file = controls.split(" ")[1].split("/");
		String path = path_file[0];
		String file = path_file[path_file.length - 1];

		WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);

		Response r = target.path(username).path(path).request().header(HttpHeaders.AUTHORIZATION, token)
				.accept(MediaType.APPLICATION_JSON).post(Entity.entity(file, MediaType.APPLICATION_JSON));

		if (r.getStatus() == Status.OK.getStatusCode())
			System.out.println("File " + file + " now in path " + path);
		else
			System.out.println(r.getStatus() + " - error creating path " + path);

	}

	private static void operationGet(String controls) {
		String username = controls.split(" ")[0];
		String[] path_file = controls.split(" ")[1].split("/");
		String path = path_file[0];
		String file = path_file[path_file.length - 1];

		WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);

		Response r = target.path(username).path(path).path(file).request().header(HttpHeaders.AUTHORIZATION, token)
				.accept(MediaType.APPLICATION_JSON).get();

		if (r.getStatus() == Status.OK.getStatusCode()) {
			String fileResponse = r.readEntity(new GenericType<String>() {
			});
			System.out.println(fileResponse);
		} else
			System.out.println(r.getStatus() + " - error getting file " + file + " from path " + path);
	}

	private static void operationCp(String controls) {
		String username = controls.split(" ")[0];
		String[] path_file = controls.split(" ")[1].split("/");
		String[] path2_fil2 = controls.split(" ")[2].split("/");
		String path = path_file[0];
		String file = path_file[path_file.length - 1];
		String path2 = path2_fil2[0];
		String file2 = path2_fil2[path2_fil2.length - 1];
		FilesToCopy filesToCopy = new FilesToCopy(path, file, path2, file2);

		WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);

		Response r = target.path(username).request().header(HttpHeaders.AUTHORIZATION, token)
				.accept(MediaType.APPLICATION_JSON).post(Entity.entity(filesToCopy, MediaType.APPLICATION_JSON));

		if (r.getStatus() == Status.OK.getStatusCode())
			System.out.println("File " + path + "/" + file + " copied to the file " + path2 + "/" + file2);
		else
			System.out.println(r.getStatus() + " - error coping " + path + "/" + file + " to " + path2 + "/" + file2);
	}

	private static void operationRm(String controls) {
		String username = controls.split(" ")[0];
		String[] path_file = controls.split(" ")[1].split("/");
		String path = path_file[0];
		String file = path_file[path_file.length - 1];

		WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);

		Response r = target.path(username).path(path).path(file).request().header(HttpHeaders.AUTHORIZATION, token)
				.accept(MediaType.APPLICATION_JSON).delete();

		if (r.getStatus() == Status.OK.getStatusCode())
			System.out.println(path + "/" + file + " deleted!");
		else
			System.out.println(r.getStatus() + " - error removing " + path + "/" + file);
	}

	private static void operationRmdir(String controls) {
		String username = controls.split(" ")[0];
		String path = controls.split(" ")[1];

		WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);

		Response r = target.path(username).path(path).request().header(HttpHeaders.AUTHORIZATION, token)
				.accept(MediaType.APPLICATION_JSON).delete();

		if (r.getStatus() == Status.OK.getStatusCode())
			System.out.println(path + " deleted!");
		else
			System.out.println(r.getStatus() + " - error removing " + path);
	}

	private static void operationFile(String controls) {
		String username = controls.split(" ")[0];
		String[] path_file = controls.split(" ")[1].split("/");
		String path = path_file[0];
		String file = path_file[path_file.length - 1];

		WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);

		Response r = target.path("file").path(username).path(path).path(file).request()
				.header(HttpHeaders.AUTHORIZATION, token).accept(MediaType.APPLICATION_JSON).get();

		if (r.getStatus() == Status.OK.getStatusCode()) {
			List<String> listOfAttr = r.readEntity(new GenericType<List<String>>() {
			});
			for (String attr : listOfAttr) {
				System.out.println(attr);
			}
		} else
			System.out.println(r.getStatus() + " - error while listing file!");
	}

	private static void operationCreateUserFolder(String username) {
        WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);

        Response r = target.path("createUserDir").path(username).request().accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(username, MediaType.APPLICATION_JSON));

        if (r.getStatus() == Status.OK.getStatusCode())
            System.out.println("Path " + username + " created successfully!");
        else
            System.out.println(r.getStatus() + " - error creating path " + username);
    }
	
	
	// -----------------------------------DH------------------------------------------//

	private static AServer DHCall(String username, String password) throws Exception {
		PublicNumDH pDH = null;
		// First part of the aggrement
		// send username
		synchronized (username) {
			WebTarget target = client.target(SERVER_URL).path(Auth.PATH);

			Response r = target.path("dh").path(username).request().accept(MediaType.APPLICATION_OCTET_STREAM).get();

			if (r.getStatus() == Status.OK.getStatusCode()) {
				byte[] byte_pDH = r.readEntity(new GenericType<byte[]>() {
				});
				pDH = (PublicNumDH) Utils.convertFromBytes(byte_pDH);
			} else {
				System.out.println(r.getStatus() + " - failed aggreement...");
				return null;
			}
		}

		// starts aggrement
		DHClient dhC = new DHClient();
		KeyPair yClient = dhC.init();

		// Receives PublicKey -> pDH.getYaserver()
		byte[] key = dhC.finish(pDH.getYaserver());
		SecretKey Ks = new SecretKeySpec(key, 0, key.length, "AES");

		// Prepares Response -> PWD || random + 1
		Password PWD = new Password(password, pDH.getRandom().nextInt());

		// encrypt password -> {H(PWD || random +1)}Ks
		byte[] encPassword = encPassword(PWD, Ks, yClient.getPrivate());

		SecureRandom random2 = new SecureRandom();
		// send response to the server
		ResponsePNDH response = new ResponsePNDH(encPassword, random2, yClient);

		WebTarget target = client.target(SERVER_URL).path(Auth.PATH);

		// get the token, ttl, A and credentials
		byte[] aServerbytes = target.path("dh").request().accept(MediaType.APPLICATION_OCTET_STREAM)
				.post(Entity.entity(Utils.convertToBytes(response), MediaType.APPLICATION_OCTET_STREAM), new GenericType<byte[]>() {
				});
		AServer aServer = (AServer) Utils.convertFromBytes(aServerbytes);
		return aServer;
	}

	private static byte[] encPassword(Password PWD, Key Ks, Key privateKey) throws Exception {
		byte[] PWDBytes = Utils.convertToBytes(PWD);
		// Hash it
		MessageDigest sh = MessageDigest.getInstance("SHA-512");
		byte[] hashedPWD = sh.digest(PWDBytes);

		// cipher it
		//Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, Ks);

		byte[] encPassword = cipher.doFinal(hashedPWD);
		return encPassword;
	}

	private static void test() {
		WebTarget target = client.target(SERVER_URL).path("test");
		
		Response r = target.request().accept(MediaType.APPLICATION_JSON).get();

		if (r.getStatus() == Status.OK.getStatusCode()) {
			boolean b = r.readEntity(boolean.class);
			System.out.println(b);
		} else {
			System.out.println(r.getStatus() + " - failed aggreement...");
		}
	}

}
