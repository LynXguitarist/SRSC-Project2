package utils;

import java.io.Serializable;
import java.security.SecureRandom;

public class ResponsePNDH implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5851247608338959806L;
	
	// Response from the client to the aggreement(PublicKey)

	// with H and Ks
	private byte[] encPassword;
	private SecureRandom random2;
	private String yClient;

	public ResponsePNDH(byte[] encPassword, SecureRandom random2, String yClient) {
		this.encPassword = encPassword;
		this.random2 = random2;
		this.yClient = yClient;
	}

	public byte[] getEncPassword() {
		return encPassword;
	}

	public SecureRandom getRandom2() {
		return random2;
	}

	public String getyClient() {
		return yClient;
	}

}
