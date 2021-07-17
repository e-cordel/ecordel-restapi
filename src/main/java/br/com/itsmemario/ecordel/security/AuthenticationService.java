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

import br.com.itsmemario.ecordel.security.jwt.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

	public static final String BEARER = "Bearer";
	public static final String ROLES = "roles";

	private static final String ISSUER = "e-cordel";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserRepository repository;
	private final JwtToken jwtToken;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<CordelUser> user = repository.findByUsername(username);
		if (user.isPresent()) {
			return user.get();
		}
		throw new UsernameNotFoundException("User not found!");
	}

	public TokenDto generateToken(Authentication authentication) {
		
		CordelUser principal = (CordelUser) authentication.getPrincipal();
		
		Date today = new Date();
		Date expiration = new Date(today.getTime() + jwtToken.getExpiration());
		
		String token = Jwts.builder()
			.setIssuer(ISSUER)
			.setSubject(principal.getId().toString())
			.claim(ROLES, principal.getAuthorityNames())
			.setIssuedAt(today)
			.setExpiration(expiration)
			.signWith(SignatureAlgorithm.HS256, jwtToken.getSecretKey())
			.compact();

		return new TokenDto(token, BEARER, expiration.getTime());
	}

	public boolean isValidToken(String token) {
		try {
			return Jwts.parser()
					.setSigningKey(jwtToken.getSecretKey())
					.requireIssuer(ISSUER)
					.parseClaimsJws(token)!=null;
		}catch(io.jsonwebtoken.SignatureException e) {
			return false;
		}
	}

	public Optional<CordelUser> getUserFromToken(String token) {
		try {
			Claims body = Jwts.parser().setSigningKey(jwtToken.getSecretKey()).parseClaimsJws(token).getBody();
			Long id = Long.parseLong(body.getSubject());
			return repository.findById(id);
		} catch (io.jsonwebtoken.SignatureException e) {
			logger.warn("No possible to parser token: {}", e.getMessage());
			return Optional.empty();
		}
	}
	
	
}
