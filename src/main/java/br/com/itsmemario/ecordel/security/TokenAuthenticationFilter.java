package br.com.itsmemario.ecordel.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class TokenAuthenticationFilter extends OncePerRequestFilter{
	
	private AuthenticationService authenticationService;
	
	TokenAuthenticationFilter(AuthenticationService authenticationService) {
		super();
		this.authenticationService = authenticationService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		Optional<String> token = getToken(request);
		
		if(isValidToken(token)) {
			authorizeRequestWithToken(token.get());
		}
		
		filterChain.doFilter(request, response);
		
	}

	private void authorizeRequestWithToken(String token) {
		Optional<ECorderlUser> userFromToken = authenticationService.getUserFromToken(token);
		if(userFromToken.isPresent()) {
			ECorderlUser user = userFromToken.get();
			System.out.println("authorizing "+user.getUsername());
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);		
		}
	
	}

	private boolean isValidToken(Optional<String> token) {
		return token.isPresent() && authenticationService.isValidToken(token.get());
	}

	private Optional<String> getToken(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");
		
		if(token!=null) {
			token = token.substring(7);
		}
		
		return Optional.ofNullable(token);
		
	}

}
