package fileStorage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import utils.File;
import utils.FilesToCopy;

public class TestingFileStorage {

	private static List<MyTree> trees;
	
	// USA ISTO
	private static Properties prop;
	private static final String PATH = "access.conf";

	public static void main(String[] args) throws IOException {
		trees = new LinkedList<>();
		trees.add(new MyTree(new MyNode("fred", "fred"), new MyNode("fred", "fred")));
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String line = in.readLine();
			String operation = line.split(" ")[0];
			// rest of the line -> after the operation + username
			String controls = line.substring(operation.length() + 1);

			switch (operation) {
			case "ls":
				mainLs(controls);
				break;
			case "mkdir":
				mainMkdir(controls);
				break;
			case "put":
				mainPut(controls);
				break;
			case "get":
				mainGet(controls);
				break;
			case "cp":
				mainCp(controls);
				break;
			case "rm":
				mainRm(controls);
				break;
			case "rmdir":
				mainRmdir(controls);
				break;
			case "file":
				mainFile(controls);
				break;
			case "prop":
				prop();
			default:
				return;
			}
		}
	}

	private static void mainLs(String controls) {
		String[] split = controls.split(" ");
		String username = split[0];
		if (split.length == 2) {
			String path = split[1];
			ls(username, path);
		} else {
			ls(username);
		}
	}

	private static void mainMkdir(String controls) {
		String[] split = controls.split(" ");
		String username = split[0];
		String path = split[1];
		mkdir(username, path);
	}

	private static void mainPut(String controls) {
		String[] split = controls.split(" ");
		String username = split[0];
		String path = split[1];
		put(username, path);
	}

	private static void mainGet(String controls) {
		String[] split = controls.split(" ");
		String username = split[0];
		String path = split[1];
		get(username, path);
	}

	private static void mainCp(String controls) {
		String[] split = controls.split(" ");
		String username = split[0];

		String path = split[1];
		String[] path_file = path.split("/");
		String file = path_file[path_file.length - 1];

		String path2 = split[2];
		String[] path2_file2 = path2.split("/");
		String file2 = path2_file2[path2_file2.length - 1];

		FilesToCopy fileToCopy = new FilesToCopy(path, file, path2, file2);
		cp(username, fileToCopy);
	}

	private static void mainRm(String controls) {
		String[] split = controls.split(" ");
		String username = split[0];
		String path = split[1];
		rm(username, path);
	}

	private static void mainRmdir(String controls) {

	}

	private static void mainFile(String controls) {

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

	public static List<String> ls(String username, String path) {
		List<String> children = new LinkedList<>();
		for (MyTree tree : trees) {
			if (tree.hasPath(username + "/" + path)) {
				children = tree.getChildrenFromNode(path);
				break;
			}
		}
		for (String s : children)
			System.out.println(s);

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

	public static void put(String username, String path) {
		String parentPath = username;

		String[] pathSplit = path.split("/");

		for (int i = 0; i < pathSplit.length - 1; i++) {
			parentPath += "/" + pathSplit[i];
		}

		for (MyTree tree : trees) {
			if (tree.hasPath(parentPath)) {
				String fileName = tree.addElement(username, path);
				tree.addFile(fileName, username + "/" + path, null); // ver como meter os binarios
				break;
			}
		}
	}

	public static File get(String username, String path) {
		String[] path_split = path.split("/");
		String fileName = path_split[path_split.length - 1];
		File file = null;
		for (MyTree tree : trees) {
			File f = tree.getFileByName(fileName);
			if (f == null || f.getPath().equals(" ") || f.getPath() == null)
				break;

			if (f.getPath().equals(username + "/" + path)) {
				file = f;
				break;
			}
		}
		return file;
	}

	public static void cp(String username, FilesToCopy fileToCopy) {
		String path = fileToCopy.getPath();
		String path2 = fileToCopy.getPath2();
		String file = fileToCopy.getFile();
		String file2 = fileToCopy.getFile2();

		String parentPath2 = "";
		String[] pathSplit = path2.split("/");
		for (int i = 0; i < pathSplit.length; i++) {
			parentPath2 += pathSplit[i] + "/";
		}
		// if the path or file or path2 or file2 doesn't exist
		if (get(username, path) == null || get(username, parentPath2) == null)
			return;

		for (MyTree tree : trees) {
			File f = tree.getFileByName(file);
			if (f == null)
				break;

			if (f.getPath().equals(username + "/" + path)) {
				tree.addElement(username, path2 + "/" + file2);
				tree.addFile(file2, username + "/" + path2, f.getBinary());
				break;
			}

		}
	}

	public static void rm(String username, String path) {
		String[] path_split = path.split("/");
		String fileName = path_split[path_split.length - 1];
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

	public static void rmdir(String username, String path) {
		// TODO Auto-generated method stub

	}

	public static List<String> file(String username, String path, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static final void prop() {
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

}
