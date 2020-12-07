package fileStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyNode {

	List<MyNode> childs;
	private String value; // name of the path or file
	private String incrementalPath;

	public MyNode(String value, String incrementalPath) {
		childs = new ArrayList<>();
		this.value = value;
		this.incrementalPath = incrementalPath;
	}

	public void addElement(String currentPath, String[] list) {
		currentPath += "/" + list[0];

		MyNode currentChild = new MyNode(list[0], currentPath);
		int index = childs.indexOf(currentChild);
		if (index == -1) {
			childs.add(currentChild);
		} else {
			MyNode nextChild = childs.get(index);
			nextChild.addElement(currentPath, Arrays.copyOfRange(list, 1, list.length));
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

	@Override
	public boolean equals(Object obj) {
		MyNode cmpObj = (MyNode) obj;
		return incrementalPath.equals(cmpObj.incrementalPath) && value.equals(cmpObj.value);
	}

}
