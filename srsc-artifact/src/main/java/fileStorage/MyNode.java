package fileStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyNode {

	List<MyNode> childs;
	List<MyNode> leafs;
	private String value; // name of the path or file
	private String incrementalPath;

	public MyNode(String value, String incrementalPath) {
		childs = new ArrayList<>();
		leafs = new ArrayList<>();
		this.value = value;
		this.incrementalPath = incrementalPath;
	}

	public boolean isLeaf() {
		return childs.isEmpty() && leafs.isEmpty();
	}

	public void addElement(String currentPath, String[] list) {
		while (list[0] == null || list[0].equals(""))
			list = Arrays.copyOfRange(list, 1, list.length);

		MyNode currentChild = new MyNode(list[0], currentPath + "/" + list[0]);
		if (list.length == 1) {
			leafs.add(currentChild);
			return;
		} else {
			int index = childs.indexOf(currentChild);
			if (index == -1) {
				childs.add(currentChild);
				currentChild.addElement(currentChild.incrementalPath, Arrays.copyOfRange(list, 1, list.length));
			} else {
				MyNode nextChild = childs.get(index);
				nextChild.addElement(currentChild.incrementalPath, Arrays.copyOfRange(list, 1, list.length));
			}
		}
	}

	public MyNode getChildByName(String name) {
		MyNode child = null;
		for (MyNode node : childs) {
			if (node.getValue().equals(name))
				child = node;
		}
		return child;
	}

	public String getIncrementPath() {
		return incrementalPath;
	}

	public String getValue() {
		return value;
	}

}
