package com.example.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.security.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
	Optional<CustomerEntity> findByEmail(String email);
}
