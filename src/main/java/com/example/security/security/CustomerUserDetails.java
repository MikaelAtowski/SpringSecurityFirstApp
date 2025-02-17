package com.example.security.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.security.repository.CustomerRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CustomerUserDetails implements UserDetailsService{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.customerRepository.findByEmail(username)
				.map(customer -> {
					var authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
					return new User(customer.getEmail(), customer.getPassword(), authorities);
				}).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
	
}
