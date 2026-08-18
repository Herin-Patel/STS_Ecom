package com.ecom.repository;

import com.ecom.model.UserDtls;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {
	public UserDtls findByEmail(String email);

	public List<UserDtls> findByRole(String role);

	public UserDtls findByResetToken(String token);

	public Page<UserDtls> findByRole(String role, Pageable pageableObj);
}
