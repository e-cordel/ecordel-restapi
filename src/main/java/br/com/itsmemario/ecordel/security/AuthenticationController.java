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

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	private final AuthenticationProvider provider;
	private final AuthenticationService authenticationService;
	
	@Autowired
	public AuthenticationController(AuthenticationProvider provider, AuthenticationService authenticationService) {
		this.provider = provider;
		this.authenticationService = authenticationService;
	}

	@PostMapping
	public ResponseEntity<TokenDto> authenticate(@RequestBody @Valid LoginData loginData){
		logger.info("Login attempt, user: {}", loginData.getUsername());
		try {
			UsernamePasswordAuthenticationToken authenticationToken = loginData.toAuthenticationToken();
			Authentication authentication = provider.authenticate(authenticationToken);
			TokenDto tokenDto = authenticationService.generateToken(authentication);
			return ResponseEntity.ok(tokenDto);
		} catch (AuthenticationException e) {
			logger.info("Authentication failed for user: {}", loginData.getUsername());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

}
