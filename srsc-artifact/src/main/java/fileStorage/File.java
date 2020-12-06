package fileStorage;

public class File {

	private String name;
	private String path;
	private byte[] binary;

	public File(String name, String path, byte[] binary) {
		this.setName(name);
		this.setBinary(binary);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getBinary() {
		return binary;
	}

	public void setBinary(byte[] binary) {
		this.binary = binary;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
