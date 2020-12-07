package fileStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import client.FilesToCopy;

public class TestingFileStorage {

	private static List<MyTree> trees;

	public static void main(String[] args) throws IOException {
		trees = new LinkedList<>();
		trees.add(new MyTree(new MyNode("fred", "fred"), new MyNode("fred", "fred")));
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String line = in.readLine();
			String operation = line.split(" ")[0];
			// rest of the line -> after the operation + username
			String[] username_controls = line.substring(operation.length() + 1).split(" ");
			String username = username_controls[0];
			String controls = "";
			if (username_controls.length > 1)
				controls = username_controls[1];
			else
				controls = username;

			switch (operation) {
			case "ls":
				ls(controls);
				break;
			case "mkdir":
				mkdir(username, controls);
				break;
			/*
			 * case "put": put(controls); break; case "get": operationGet(controls); break;
			 * case "cp": operationCp(controls); break; case "rm": operationRm(controls);
			 * break; /*case "rmdir": operationRmdir(controls); break; case "file":
			 * operationFile(controls); break
			 */

			default:
				return;
			}
		}
	}

	public static List<String> ls(String username) {
		List<String> children = new LinkedList<>();
		for (MyTree tree : trees) {
			MyNode root = tree.getRoot();
			if (root.getValue().equals(username)) {
				// root -> get all children
				// forEach one add to the list to be returned
				for (MyNode node : root.childs) {
					children.add(node.getValue());
					System.out.println(node.getValue());
				}
				break;
			}
		}
		return children;
	}

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

	public static void mkdir(String username, String path) {
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

	public void put(String username, String path, String fileName) {
		for (MyTree tree : trees) {
			if (tree.hasPath(path)) {
				tree.addElement(username, path + "/" + fileName);
				tree.addFile(fileName, username + "/" + path, null); // ver como meter os binarios
				break;
			}
		}
	}

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

	public void rmdir(String username, String path) {
		// TODO Auto-generated method stub

	}

	public List<String> file(String username, String path, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

}
