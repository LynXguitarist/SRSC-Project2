package fileStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;

import api.AccessControl;
import api.FileStorage;
import utils.File;
import utils.FilesToCopy;

public class FileStorageService implements FileStorage {

	// quando registado no sistema, cria homeRoot para o username

	private static Client client;
	private String serverUrl;

	private List<MyTree> trees;
	// Saves the clients' permissions.
	private Map<String, String> sessionAuth;

	public FileStorageService(String serverUrl) {
		trees = new ArrayList<>();
		sessionAuth = new HashMap<>();

		this.serverUrl = serverUrl;
		ClientConfig config = new ClientConfig();
		client = ClientBuilder.newClient(config);
	}

	@Override
	public List<String> ls(String username, HttpHeaders headers) {
		if (getAuth(username, headers).equals("deny"))
			throw new WebApplicationException(Status.FORBIDDEN);

		List<String> children = new LinkedList<>();
		for (MyTree tree : trees) {
			MyNode root = tree.getRoot();
			if (root.getValue().equals(username)) {
				// root -> get all children
				// forEach one add to the list to be returned
				for (MyNode node : root.childs) {
					children.add(node.getValue());
				}
				break;
			}
		}
		if (children.isEmpty())
			throw new WebApplicationException(Status.NOT_FOUND);

		return children;
	}

	@Override
	public List<String> ls(String username, String path, HttpHeaders headers) {
		if (getAuth(username, headers).equals("deny"))
			throw new WebApplicationException(Status.FORBIDDEN);

		List<String> children = new LinkedList<>();
		for (MyTree tree : trees) {
			if (tree.hasPath(path)) {
				children = tree.getChildrenFromNode(path);
				break;
			}
		}
		if (children.isEmpty())
			throw new WebApplicationException(Status.NOT_FOUND);

		return children;
	}

	@Override
	public void mkdir(String username, String path, HttpHeaders headers) {
		boolean addedDir = false;

		if (!getAuth(username, headers).equals("allow read write"))
			throw new WebApplicationException(Status.FORBIDDEN);

		// verifies if the path has a file
		// if has a file, it is an incorrect path
		if (path.contains("."))
			return;

		String parentPath = username;

		String[] pathSplit = path.split("/");
		for (int i = 0; i < pathSplit.length - 1; i++) {
			parentPath += "/" + pathSplit[i];
		}
		for (MyTree tree : trees) {
			if (tree.hasPath(parentPath)) {
				// add directory to path -> user name/path
				// (directory is the last element of the path)
				tree.addElement(username, path);
				addedDir = true;
				break;
			}
		}
		// if dir wans't added it's because the path doesn't exists
		if (!addedDir)
			throw new WebApplicationException(Status.NOT_FOUND);
	}

	@Override
	public void put(String username, String path, String fileName, HttpHeaders headers) {
		boolean addedFile = false;

		if (!getAuth(username, headers).equals("allow read write"))
			throw new WebApplicationException(Status.FORBIDDEN);

		for (MyTree tree : trees) {
			if (tree.hasPath(path)) {
				tree.addElement(username, path + "/" + fileName);
				tree.addFile(fileName, username + "/" + path, null); // ver como meter os binarios
				addedFile = true;
				break;
			}
		}
		// if file wans't added it's because the path doesn't exists
		if (!addedFile)
			throw new WebApplicationException(Status.NOT_FOUND);
	}

	@Override
	public File get(String username, String path, String fileName, HttpHeaders headers) {
		if (getAuth(username, headers).equals("deny"))
			throw new WebApplicationException(Status.FORBIDDEN);

		File file = null;
		for (MyTree tree : trees) {
			File f = tree.getFileByName(fileName);
			if (f.getPath().equals(username + "/" + path)) {
				file = f;
				break;
			}
		}
		if (file == null)
			throw new WebApplicationException(Status.NOT_FOUND);

		return file;
	}

	@Override
	public void cp(String username, FilesToCopy fileToCopy, HttpHeaders headers) {
		boolean fileCopied = false;

		if (!getAuth(username, headers).equals("allow read write"))
			throw new WebApplicationException(Status.FORBIDDEN);

		String path = fileToCopy.getPath();
		String path2 = fileToCopy.getPath2();
		String file = fileToCopy.getFile();
		String file2 = fileToCopy.getFile2();
		// if the path or file or path2 or file2 doesn't exist
		if (get(username, path2, file2, headers) == null || get(username, path, file, headers) == null)
			return;

		for (MyTree tree : trees) {
			File f = tree.getFileByName(file);
			if (f != null) {
				if (f.getPath().equals(username + "/" + path)) {
					tree.addElement(username, path2 + "/" + file2);
					tree.addFile(file2, username + "/" + path2, tree.getFileByName(file).getBinary());
					fileCopied = true;
					break;
				}
			}
		}
		// If filed wasn't copied, the path doesn't exist
		if (!fileCopied)
			throw new WebApplicationException(Status.NOT_FOUND);
	}

	@Override
	public void rm(String username, String path, String fileName, HttpHeaders headers) {
		boolean wasRemoved = false;

		if (!getAuth(username, headers).equals("allow read write"))
			throw new WebApplicationException(Status.FORBIDDEN);

		for (MyTree tree : trees) {
			for (File f : tree.files) {
				if (f.getName().equals(fileName) && f.getPath().equals(username + "/" + path)) {
					tree.removeFile(f, path);
					wasRemoved = true;
					break;
				}
			}
		}
		// If it wasn't removed, the path/file doesn't exist
		if (!wasRemoved)
			throw new WebApplicationException(Status.NOT_FOUND);
	}

	// OPTIONALS

	@Override
	public void rmdir(String username, String path, HttpHeaders headers) {
		if (!getAuth(username, headers).equals("allow read write"))
			throw new WebApplicationException(Status.FORBIDDEN);

		// TODO Auto-generated method stub

	}

	@Override
	public List<String> file(String username, String path, String fileName, HttpHeaders headers) {
		if (!getAuth(username, headers).equals("allow read write"))
			throw new WebApplicationException(Status.FORBIDDEN);

		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------Client_Calls------------------------------------------//

	private String getAuth(String username, HttpHeaders headers) {
		String permissions = sessionAuth.get(username);

		String token = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
		// gets permissions for the user
		WebTarget target = client.target(serverUrl).path(AccessControl.PATH);
		Response r = target.path(username).request().header(HttpHeaders.AUTHORIZATION, token)
				.accept(MediaType.APPLICATION_JSON).get();

		if (r.getStatus() == Status.OK.getStatusCode()) {
			if (permissions != null)
				return permissions;
			else if (!r.hasEntity())
				System.out.println("The body is empty!");
			else {
				permissions = r.readEntity(new GenericType<String>() {
				});
			}
		}
		// saves user's permissions
		sessionAuth.put(username, permissions);
		return sessionAuth.get(username);
	}

}
