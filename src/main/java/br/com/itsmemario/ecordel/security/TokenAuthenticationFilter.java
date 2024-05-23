/*
 * Copyright 2020 Projeto e-cordel (http://ecordel.com.br)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package br.com.itsmemario.ecordel.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class TokenAuthenticationFilter extends OncePerRequestFilter{

	private final Logger tokenAuthLogger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

	public static final String AUTHORIZATION = "Authorization";
	private final AuthenticationService authenticationService;
	
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

	private Optional<String> getToken(HttpServletRequest request) {

		String token = request.getHeader(AUTHORIZATION);

		if(token!=null) {
			token = token.substring(7);
		}

		return Optional.ofNullable(token);

	}

	private boolean isValidToken(Optional<String> token) {
		return token.isPresent() && authenticationService.isValidToken(token.get());
	}

	private void authorizeRequestWithToken(String token) {
		Optional<CordelUser> userFromToken = authenticationService.getUserFromToken(token);
		if(userFromToken.isPresent()) {
			CordelUser user = userFromToken.get();
			tokenAuthLogger.info("authorizing {}",user.getUsername());
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	
	}


}
