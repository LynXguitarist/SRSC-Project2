package utils;

import java.io.Serializable;

public class KToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6985804936039717815L;

	private String A;
	private String kToken1024;
	private long ttl;

	public KToken(String A, String kToken1024, long ttl) {
		this.A = A;
		this.kToken1024 = kToken1024;
		this.ttl = ttl;
	}

	public String getA() {
		return A;
	}

	public String getkToken1024() {
		return kToken1024;
	}

	public long getTtl() {
		return ttl;
	}

}
