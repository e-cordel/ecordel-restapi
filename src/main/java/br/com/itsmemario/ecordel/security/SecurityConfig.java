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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final BCryptPasswordEncoder encoder;
	private final AuthenticationService authenticationService;
	
	@Autowired
	public SecurityConfig(BCryptPasswordEncoder encoder, AuthenticationService authenticationService) {
		this.encoder = encoder;
		this.authenticationService = authenticationService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> corsConfigurationSource())
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(req -> {
				req.requestMatchers(HttpMethod.POST, "/auth").permitAll();
				req.requestMatchers(HttpMethod.GET, "/**").permitAll();
				req.requestMatchers(HttpMethod.POST, "/cordels/**").hasAnyAuthority(CordelAuthority.ADMIN, CordelAuthority.AUTHOR, CordelAuthority.EDITOR);
				req.requestMatchers(HttpMethod.PUT, "/cordels/**").hasAnyAuthority(CordelAuthority.ADMIN, CordelAuthority.AUTHOR, CordelAuthority.EDITOR);
				req.requestMatchers(HttpMethod.POST, "/authors/**").hasAnyAuthority(CordelAuthority.ADMIN);
				req.requestMatchers(HttpMethod.PUT, "/authors/**").hasAnyAuthority(CordelAuthority.ADMIN);
				req.anyRequest().authenticated();
			})
			.addFilterBefore(new TokenAuthenticationFilter(authenticationService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		 return web -> web.ignoring()
	        .requestMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
	}

	/**
	 * Deprecated used only to generate passwords locally
	 * @param args
	 */
	public static void main(String[] args) {
		// System.out.println(new BCryptPasswordEncoder().encode("admin"));
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(authenticationService);
		authenticationProvider.setPasswordEncoder(encoder);
		return authenticationProvider;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.addExposedHeader(HttpHeaders.LOCATION);
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
