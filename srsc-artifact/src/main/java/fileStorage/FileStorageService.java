package fileStorage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import api.FileStorage;
import frontend.FilesToCopy;

public class FileStorageService implements FileStorage {

	// quando registado no sistema, cria homeRoot para o username
	private List<MyTree> trees;

	public FileStorageService() {
		trees = new ArrayList<>();
	}

	@Override
	public List<String> ls(String username) {
		List<String> childs = new LinkedList<>();
		for (MyTree tree : trees) {
			MyNode commonRoot = tree.getCommonRoot();
			if (commonRoot.getValue().equals(username)) {
				// commonRoot -> get all childs
				// forEach one add to the list to be returned
				for (MyNode node : commonRoot.childs) {
					childs.add(node.getValue());
				}
			}
		}
		return childs;
	}

	@Override
	public List<String> ls(String username, String path) {
		List<String> childs = new LinkedList<>();
		for (MyTree tree : trees) {
			MyNode root = tree.getRoot();
			if (root.getIncrementPath().equals(username + "/" + path)) {
				for (MyNode node : root.childs) {
					childs.add(node.getValue());
				}
			}
		}
		return childs;
	}

	@Override
	public void mkdir(String username, String path) {
		// ta mal, melhora burro
		for (MyTree tree : trees) {
			MyNode root = tree.getRoot();
			if (root.getIncrementPath().equals(username + "/" + path)) {
				// add dir to path -> username/path
				root.addElement(root.getIncrementPath(), path.split("/"));
			}
		}

	}

	@Override
	public void put(String username, String path, String file) {
		// TODO Auto-generated method stub

	}

	@Override
	public String get(String username, String path, String file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cp(String username, FilesToCopy fileToCopy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rm(String username, String path, String file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rmdir(String username, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> file(String username, String path, String file) {
		// TODO Auto-generated method stub
		return null;
	}

}
