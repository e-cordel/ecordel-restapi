package br.com.itsmemario.ecordel.security;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class ECorderlAuthority implements GrantedAuthority{
	
	public static final String COMMON_USER = "USER";
	public static final String ADMIN = "ADMIN";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String authority;
	
	ECorderlAuthority() {}

	public ECorderlAuthority(String autority) {
		this.authority = autority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
	
	public static ECorderlAuthority commonUser() {
		return new ECorderlAuthority(COMMON_USER);
	}

}
