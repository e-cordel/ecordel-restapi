package br.com.itsmemario.ecordel.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private AuthenticationService authenticationService;
	
	@Autowired
	public SecurityConfig(AuthenticationService authenticationService) {
		super();
		this.authenticationService = authenticationService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService) .passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//h2;console only
		http.headers().frameOptions().disable()
			.and()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/auth").permitAll()
			.antMatchers(HttpMethod.GET, "/authors/**").permitAll()
			.antMatchers(HttpMethod.GET, "/cordels/**").permitAll()
			.antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
			.anyRequest().authenticated()
			.and().csrf().disable()// TODO review
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().addFilterBefore(new TokenAuthenticationFilter(authenticationService), UsernamePasswordAuthenticationFilter.class);

	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		 web.ignoring()
	        .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
		
	}
	
	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("admin"));
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
}
