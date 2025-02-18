package com.example.security.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ApiKeyFilter extends OncePerRequestFilter {

	private static final String API_KEY = "my_key";
	private static final String API_KEY_HEADER = "api_key";

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			final var apiKeyOptional = Optional.of(request.getHeader(API_KEY_HEADER));
			final var apiKey = apiKeyOptional.orElseThrow(() -> new BadCredentialsException("No header api key"));
		
			if(apiKey.equals(API_KEY)) {
				throw new BadCredentialsException("Invalid api key");
			}
			
		} catch(Exception e) {
			throw new BadCredentialsException("Invalid api key");
		}
		
		filterChain.doFilter(request, response);
	}

}
