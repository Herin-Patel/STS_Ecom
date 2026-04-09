package com.ecom.repository;

import com.ecom.model.UserDetails;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDetails, Integer> {

}
