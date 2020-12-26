package fileStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import api.FileStorage;
import utils.File;
import utils.FilesToCopy;
import utils.Permissions;

public class FileStorageService implements FileStorage {

	// quando registado no sistema, cria homeRoot para o username

	private List<MyTree> trees;
	// Saves the clients' permissions.
	private Map<String, Permissions> sessionAuth;

	public FileStorageService() {
		trees = new ArrayList<>();
		sessionAuth = new HashMap<>();
	}

	@Override
	public List<String> ls(String username) {
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
		return children;
	}

	@Override
	public List<String> ls(String username, String path) {
		List<String> children = new LinkedList<>();
		for (MyTree tree : trees) {
			if (tree.hasPath(path)) {
				children = tree.getChildrenFromNode(path);
				break;
			}
		}
		return children;
	}

	@Override
	public void mkdir(String username, String path) {
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
				break;
			}
		}
	}

	@Override
	public void put(String username, String path, String fileName) {
		for (MyTree tree : trees) {
			if (tree.hasPath(path)) {
				tree.addElement(username, path + "/" + fileName);
				tree.addFile(fileName, username + "/" + path, null); // ver como meter os binarios
				break;
			}
		}
	}

	@Override
	public File get(String username, String path, String fileName) {
		File file = null;
		for (MyTree tree : trees) {
			File f = tree.getFileByName(fileName);
			if (f.getPath().equals(username + "/" + path)) {
				file = f;
				break;
			}
		}
		return file;
	}

	@Override
	public void cp(String username, FilesToCopy fileToCopy) {
		String path = fileToCopy.getPath();
		String path2 = fileToCopy.getPath2();
		String file = fileToCopy.getFile();
		String file2 = fileToCopy.getFile2();
		// if the path or file or path2 or file2 doesn't exist
		if (get(username, path2, file2) == null || get(username, path, file) == null)
			return;

		for (MyTree tree : trees) {
			File f = tree.getFileByName(file);
			if (f != null) {
				if (f.getPath().equals(username + "/" + path)) {
					tree.addElement(username, path2 + "/" + file2);
					tree.addFile(file2, username + "/" + path2, tree.getFileByName(file).getBinary());
					break;
				}
			}
		}

	}

	@Override
	public void rm(String username, String path, String fileName) {
		for (MyTree tree : trees) {
			for (File f : tree.files) {
				if (f.getName().equals(fileName) && f.getPath().equals(username + "/" + path)) {
					tree.removeFile(f, path);
					break;
				}
			}
		}
	}

	// OPTIONALS

	@Override
	public void rmdir(String username, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> file(String username, String path, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

}
