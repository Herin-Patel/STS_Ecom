package com.ecom.service.impl;

import com.ecom.model.UserDetails;
import com.ecom.service.UserService;
import com.ecom.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepositoryObj;

	@Override
	public UserDetails saveUser(UserDetails user) {
		UserDetails savedUser = userRepositoryObj.save(user);
		return savedUser;
	}
}
