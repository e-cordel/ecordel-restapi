package br.com.itsmemario.ecordel.security;

public class TokenDto {
	
	private final String token;
	private final String authenticationMethod;
	private final Long expiresAt;
	
	public TokenDto(String token, String authenticationMethod, Long expiresAt) {
		super();
		this.token = token;
		this.authenticationMethod = authenticationMethod;
		this.expiresAt = expiresAt;
	}

	public String getToken() {
		return token;
	}

	public String getAuthenticationMethod() {
		return authenticationMethod;
	}

	public Long getExpiresAt() {
		return expiresAt;
	}
}
