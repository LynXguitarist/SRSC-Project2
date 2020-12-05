package fileStorage;

public class MyTree {

	private MyNode root;
	private MyNode commonRoot;

	public MyTree(MyNode root, MyNode commonRoot) {
		this.root = root;
		this.commonRoot = commonRoot;
	}

	public void addElement(String value) {
		String[] list = value.split("/");

		// latest element of the list is the file
		root.addElement(root.getIncrementPath(), list);
	}

	public MyNode getCommonRoot() {
		return commonRoot;
	}
	
	public MyNode getRoot() {
		return root;
	}

	public void printTree() {
		// don't know if i will do it
	}
}
