package utils;

import java.io.Serializable;

public class Password implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8875601702921024533L;
	
	private String password;
	private int random;

	public Password(String password, int random) {
		this.password = password;
		this.random = random;
	}

	public String getPassword() {
		return password;
	}

	public int getRandom() {
		return random;
	}

}
