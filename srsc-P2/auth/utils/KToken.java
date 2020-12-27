package utils;

import java.io.Serializable;

public class KToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6985804936039717815L;

	// A||Ktoken1024||TTL||other-credential-att)

	private String A;
	private String kToken1024;
	private long ttl;
	private String other;

	public KToken(String A, String kToken1024, long ttl, String other) {
		this.A = A;
		this.kToken1024 = kToken1024;
		this.ttl = ttl;
		this.other = other;
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

	public String getOther() {
		return other;
	}
}
