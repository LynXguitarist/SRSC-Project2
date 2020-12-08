package fileStorage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyTree {

	private MyNode root;
	private MyNode homeRoot;
	List<File> files;

	public MyTree(MyNode root, MyNode homeRoot) {
		this.root = root;
		this.homeRoot = homeRoot;
		this.files = new ArrayList<>();
	}

	public String addElement(String username, String value) {
		String[] list = value.split("/");
		if (list.length == 0)
			list[0] = value;

		return root.addElement(username, list);
	}

	/**
	 * verifies if a path exists or not parentPath -> username/restOfPath
	 * 
	 * @param parentPath
	 * @return hasPath
	 */
	public boolean hasPath(String parentPath) {
		boolean hasPath = true;

		if (parentPath.length() == 1 && homeRoot.getValue().equals(parentPath))
			return true;

		String[] path = parentPath.split("/");
		if (!path[0].equals(homeRoot.getValue()))
			return false;

		MyNode currentNode = root;
		for (int i = 1; i < path.length; i++) {
			MyNode child = currentNode.getChildByName(path[i]);
			if (child != null) {
				currentNode = child;
			} else {
				hasPath = false;
				break;
			}
		}
		return hasPath;
	}

	/**
	 * Returns a list of children of a node. node -> final node in path(last string
	 * in list split by "/")
	 * 
	 * @param path
	 * @return list of children
	 */
	public List<String> getChildrenFromNode(String path) {
		List<String> children = new LinkedList<>();
		MyNode currentNode = root;
		for (String s : path.split("/")) {
			MyNode child = currentNode.getChildByName(s);
			if (child != null)
				currentNode = child;
		}
		for (MyNode node : currentNode.childs)
			children.add(node.getValue());

		return children;
	}

	/**
	 * Removes a node from a path. node is the last String in the list split by "/"
	 * 
	 * @param path
	 */
	public void removeNode(String path) { // a/b
		String[] parentPath = path.split("/");
		if (parentPath.length <= 1) {
			MyNode child = root.getChildByName(path);
			root.childs.remove(child);
			return;
		}
		MyNode parent = root;
		for (int i = 0; i < parentPath.length - 1; i++) {
			parent = parent.getChildByName(parentPath[i]);
		}
		MyNode childToRemove = parent.getChildByName(parentPath[parentPath.length - 1]);
		parent.childs.remove(childToRemove);
	}

	public MyNode getHomeRoot() {
		return homeRoot;
	}

	public MyNode getRoot() {
		return root;
	}

	// ----------------------------File_Methods-----------------//

	public void addFile(String name, String path, byte[] binary) {
		files.add(new File(name, path, binary));
	}

	public void removeFile(File file, String path) {
		files.remove(file);
		removeNode(path);
	}

	public File getFileByName(String fileName) {
		File file = null;
		for (File f : files) {
			if (f.getName().equals(fileName)) {
				file = f;
				break;
			}
		}
		return file;
	}

}
