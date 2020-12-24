package utils;

public class FilesToCopy {

	private String path, path2;
	private String file, file2;

	public FilesToCopy(String path, String file, String path2, String file2) {
		this.setPath(path);
		this.setFile(file);
		this.setPath2(path2);
		this.setFile2(file2);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath2() {
		return path2;
	}

	public void setPath2(String path2) {
		this.path2 = path2;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFile2() {
		return file2;
	}

	public void setFile2(String file2) {
		this.file2 = file2;
	}
}
