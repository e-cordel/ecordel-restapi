package br.com.itsmemario.ecordel.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	private AuthenticationManager authenticationManager;
	private AuthenticationService authenticationService;
	
	@Autowired
	public AuthenticationController(AuthenticationManager authenticationManager, AuthenticationService authenticationService) {
		super();
		this.authenticationManager = authenticationManager;
		this.authenticationService = authenticationService;
	}
	

	@PostMapping
	public ResponseEntity<TokenDto> authenticate(@RequestBody @Valid LoginData data){
		
		logger.info("Username and password {} {}", data.getUsername(), data.getPassword());
		
		try {
			UsernamePasswordAuthenticationToken authenticationToken = data.toAuthenticationToken();
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			String token = authenticationService.generateToken(authentication); 
			
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
