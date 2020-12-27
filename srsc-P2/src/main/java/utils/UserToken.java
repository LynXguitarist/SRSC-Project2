package utils;

public class UserToken {

	private String token;
	private long ttl;

	public UserToken(String token, long ttl) {
		this.token = token;
		this.ttl = ttl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
}
