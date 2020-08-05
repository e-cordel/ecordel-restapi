package br.com.itsmemario.ecordel.security;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ECordelAuthority implements GrantedAuthority{
	
	public static final String COMMON_USER = "USER";
	public static final String ADMIN = "ADMIN";
	public static final String EDITOR = "EDITOR";
	public static final String AUTHOR = "AUTHOR";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String authority;
	
	ECordelAuthority() {}

	public ECordelAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
	
	public static ECordelAuthority commonUser() {
		return new ECordelAuthority(COMMON_USER);
	}

}
