package utils;

import java.time.LocalDateTime;

public class File {

	private String name;
	private String path;
	private String ext;
	private byte[] binary;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public File(String name, String path, byte[] binary) {
		this.name = name;
		this.path = path;
		this.binary = binary;
		this.ext = name.split(".")[1];
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
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

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
