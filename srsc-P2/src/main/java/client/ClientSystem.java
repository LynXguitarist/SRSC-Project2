package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;

import api.FileStorage;
import utils.FilesToCopy;

//Calls the services
public class ClientSystem {

	private static Client client;
	private static final String SERVER_URL = "https://localhost:8080";

	public static void main(String[] args) throws IOException {

		ClientConfig config = new ClientConfig();
		client = ClientBuilder.newClient(config);

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String line = in.readLine();
			String operation = line.split(" ")[0];
			// rest of the line -> after the operation
			String controls = line.substring(operation.length());
			switch (operation) {
			case "ls":
				operationLs(controls);
				break;
			case "Login":
				operationLogin(controls);
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
			default:
				return;
			}
		}
	}

	private static void operationLs(String controls) {
		String username = controls.split(" ")[0];
		String path = controls.split(" ")[1];

		WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);
		Response r = null;
		if (path != "")
			r = target.path(username).path(path).request().accept(MediaType.APPLICATION_JSON).get();
		else
			r = target.path(username).request().accept(MediaType.APPLICATION_JSON).get();

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

	private static void operationLogin(String controls) {
		String username = controls.split(" ")[0];
		String passowrd = controls.split(" ")[1];

	}

	private static void operationMkdir(String controls) {
		String username = controls.split(" ")[0];
		String path = controls.split(" ")[1];

		WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);

		Response r = target.path(username).request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(path, MediaType.APPLICATION_JSON));

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

		Response r = target.path(username).path(path).request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(file, MediaType.APPLICATION_JSON));

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

		Response r = target.path(username).path(path).path(file).request().accept(MediaType.APPLICATION_JSON).get();

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

		Response r = target.path(username).request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(filesToCopy, MediaType.APPLICATION_JSON));

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

		Response r = target.path(username).path(path).path(file).request().accept(MediaType.APPLICATION_JSON).delete();

		if (r.getStatus() == Status.OK.getStatusCode())
			System.out.println(path + "/" + file + " deleted!");
		else
			System.out.println(r.getStatus() + " - error removing " + path + "/" + file);
	}

	private static void operationRmdir(String controls) {
		String username = controls.split(" ")[0];
		String path = controls.split(" ")[1];

		WebTarget target = client.target(SERVER_URL).path(FileStorage.PATH);

		Response r = target.path(username).path(path).request().accept(MediaType.APPLICATION_JSON).delete();

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
				.accept(MediaType.APPLICATION_JSON).get();

		if (r.getStatus() == Status.OK.getStatusCode()) {
			List<String> listOfAttr = r.readEntity(new GenericType<List<String>>() {
			});
			for (String attr : listOfAttr) {
				System.out.println(attr);
			}
		} else
			System.out.println(r.getStatus() + " - error while listing file!");
	}

}