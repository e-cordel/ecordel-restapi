package br.com.itsmemario.ecordel.security;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "cordel_authority")
public class CordelAuthority implements GrantedAuthority{
	
	public static final String COMMON_USER = "USER";
	public static final String ADMIN = "ADMIN";
	public static final String EDITOR = "EDITOR";
	public static final String AUTHOR = "AUTHOR";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String authority;
	
	CordelAuthority() {}

	public CordelAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
	
	public static CordelAuthority commonUser() {
		return new CordelAuthority(COMMON_USER);
	}

}
