package br.com.itsmemario.ecordel.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthenticationService implements UserDetailsService{
	
	private static final String ISSUER = "e-cordel";
	private UserRepository repository;
	
	@Value("${jwt.token.expiration}")
	private Long tokenExpriation;
	
	@Value("${jwt.token.secretKey}")
	private String secretKey;
	
	@Autowired
	AuthenticationService(UserRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<ECorderlUser> user = repository.findByUsername(username);
		if (user.isPresent()) {
			return user.get();
		}
		throw new UsernameNotFoundException("User not found!");
	}

	public String generateToken(Authentication authentication) {
		
		ECorderlUser principal = (ECorderlUser) authentication.getPrincipal();
		
		Date today = new Date();
		Date expiration = new Date(today.getTime() + tokenExpriation);
		
		String token = Jwts.builder()
			.setIssuer(ISSUER)
			.setSubject(principal.getId().toString())
			.setIssuedAt(today)
			.setExpiration(expiration)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
				
		return token;
	}

	public Long getTokenExpriation() {
		return tokenExpriation;
	}

	public void setTokenExpriation(Long tokenExpriation) {
		this.tokenExpriation = tokenExpriation;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	
	
}
