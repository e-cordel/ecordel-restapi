package br.com.itsmemario.ecordel.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "cordel_user")
public class CordelUser implements UserDetails {

	public static final String USER_AUTHORITY_TABLE = "user_authority";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private boolean enabled = true;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name = USER_AUTHORITY_TABLE,
		joinColumns = @JoinColumn(name="user_id"),
		inverseJoinColumns = @JoinColumn(name="authority_id")
	)
	private Set<CordelAuthority> authorities = new HashSet<>();
	
	CordelUser() {}

	public CordelUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public Long getId() {
		return id;
	}

	public List<String> getAuthorityNames(){
		return authorities.stream().map(CordelAuthority::getAuthority).collect(Collectors.toList());
	}
	

}
