package br.com.itsmemario.ecordel.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginData {
	
	private String username;
	private String password;
	
	public LoginData() {}
	
	public LoginData(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public UsernamePasswordAuthenticationToken toAuthenticationToken() {
		return new UsernamePasswordAuthenticationToken(username, password);
	}
	
	

}
