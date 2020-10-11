package br.com.itsmemario.ecordel.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

	public static final String BEARER = "Bearer";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String ISSUER = "e-cordel";
	private UserRepository repository;
	
	@Value("${jwt.token.expiration}")
	private Long tokenExpiration;
	
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

	public TokenDto generateToken(Authentication authentication) {
		
		ECorderlUser principal = (ECorderlUser) authentication.getPrincipal();
		
		Date today = new Date();
		Date expiration = new Date(today.getTime() + tokenExpiration);
		
		String token = Jwts.builder()
			.setIssuer(ISSUER)
			.setSubject(principal.getId().toString())
			.setIssuedAt(today)
			.setExpiration(expiration)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();

		return new TokenDto(token, BEARER, expiration.getTime());
	}

	public Long getTokenExpiration() {
		return tokenExpiration;
	}

	public void setTokenExpiration(Long tokenExpiration) {
		this.tokenExpiration = tokenExpiration;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public boolean isValidToken(String token) {
		try {
			return Jwts.parser()
					.setSigningKey(secretKey)
					.requireIssuer(ISSUER)
					.parseClaimsJws(token)!=null;
		}catch(io.jsonwebtoken.SignatureException e) {
			return false;
		}
	}

	public Optional<ECorderlUser> getUserFromToken(String token) {
		try {
			Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
			Long id = Long.parseLong(body.getSubject());
			return repository.findById(id);
		} catch (io.jsonwebtoken.SignatureException e) {
			logger.warn("No possible to parser token: {}", e.getMessage());
			return Optional.empty();
		}
	}
	
	
}
