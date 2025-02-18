package com.example.security.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig{
	
	@Autowired
	private CsrfCookieFilter csrfCookieFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.addFilterBefore(new ApiKeyFilter(), BasicAuthenticationFilter.class);
		var requestHandler = new CsrfTokenRequestAttributeHandler();
		requestHandler.setCsrfRequestAttributeName("_csrf");
		http.authorizeHttpRequests(auth -> auth
				//.requestMatchers("/loans","/balance","/accounts", "/cards").authenticated()
				.requestMatchers("/loans").hasRole("LOANS")
				.requestMatchers("/balance").hasRole("BALANCE")
				.requestMatchers("/cards").hasRole("CARDS")
				.requestMatchers("/accounts").hasAnyRole("ACCOUNT")
				.anyRequest().permitAll())
			.formLogin(Customizer.withDefaults())
			.httpBasic(Customizer.withDefaults());
		http.cors(cors -> corsConfigurationSource());
		http.csrf(csrf -> csrf
				.csrfTokenRequestHandler(requestHandler)
				.ignoringRequestMatchers("/welcome", "/about_us")
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
		.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		var config = new CorsConfiguration();
		//config.setAllowedOrigins(List.of("http://localhost:4200", "http://my-app.com"));
		config.setAllowedOrigins(List.of("*"));
		//config.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
		config.setAllowedMethods(List.of("*"));
		config.setAllowedHeaders(List.of("*"));
		
		var source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		
		return source;
	}
	
	
	/*@Bean
	InMemoryUserDetailsManager inMemoryUserDetailsManager(){
		UserDetails admin = User.withUsername("admin")
				.password("to_be_encoded")
				.authorities("ADMIN")
				.build();
		
		UserDetails user = User.withUsername("user")
				.password("to_be_encoded")
				.authorities("USER")
				.build();
		
		return new InMemoryUserDetailsManager(admin, user);
	}*/
	
	/*@Bean
	UserDetailsService userDetailsService(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}*/
	
	/*@Bean
	PasswordEncoder passwordEncoder() {
	    return NoOpPasswordEncoder.getInstance();
	}*/
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}