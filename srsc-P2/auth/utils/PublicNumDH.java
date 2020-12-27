package utils;

import java.io.Serializable;
import java.security.SecureRandom;

public class PublicNumDH implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2241769907317830210L;

	private SecureRandom random;
	private String Yaserver;

	public PublicNumDH(SecureRandom random, String Yaserver) {
		this.random = random;
		this.Yaserver = Yaserver;
	}

	public SecureRandom getRandom() {
		return random;
	}

	public String getYaserver() {
		return Yaserver;
	}
}
