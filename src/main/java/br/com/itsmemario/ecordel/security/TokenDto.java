package br.com.itsmemario.ecordel.security;

public class TokenDto {
	
	private final String token;
	private final String authenticationMethod;
	
	public TokenDto(String token, String authenticationMethod) {
		super();
		this.token = token;
		this.authenticationMethod = authenticationMethod;
	}

	public String getToken() {
		return token;
	}

	public String getAuthenticationMethod() {
		return authenticationMethod;
	}
	

}
