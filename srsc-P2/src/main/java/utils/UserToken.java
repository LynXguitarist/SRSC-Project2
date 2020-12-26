package utils;

public class UserToken {

	private String token;
	private int ttl;

	public UserToken(String token, int ttl) {
		this.token = token;
		this.ttl = ttl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
}
