package utils;

import java.io.Serializable;
import java.security.SecureRandom;

public class AServer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2562525973180623883L;
	
	// A||Ktoken1024||TTL||other-credential-att))|| SecureRandom2+1
	private KToken kToken;
	private int random2;

	public AServer(KToken kToken, int random2) {
		this.kToken = kToken;
		this.random2 = random2;
	}

	public KToken getkToken() {
		return kToken;
	}

	public int getRandom2() {
		return random2;
	}
}
