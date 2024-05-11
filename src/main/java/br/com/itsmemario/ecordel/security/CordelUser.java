package br.com.itsmemario.ecordel.security;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
	@Getter
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private boolean enabled = true;
	
	@ManyToMany(fetch= FetchType.EAGER)
	@JoinTable(
		name = USER_AUTHORITY_TABLE,
		joinColumns = @JoinColumn(name="user_id"),
		inverseJoinColumns = @JoinColumn(name="authority_id")
	)
	private final Set<CordelAuthority> authorities = new HashSet<>();
	
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

    public List<String> getAuthorityNames(){
		return authorities.stream().map(CordelAuthority::getAuthority).toList();
	}
	

}
