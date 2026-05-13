package com.ecom.service.impl;

import com.ecom.model.UserDtls;
import com.ecom.service.UserService;
import com.ecom.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepositoryObj;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDtls saveUser(UserDtls user) {
		user.setRole("ROLE_USER");
		user.setIsEnable(true);

		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		UserDtls savedUser = userRepositoryObj.save(user);
		return savedUser;
	}

	@Override
	public UserDtls getUserByEmail(String email) {
		return userRepositoryObj.findByEmail(email);
	}

	@Override
	public List<UserDtls> getUsers(String role) {
		return userRepositoryObj.findByRole(role);
	}

	@Override
	public Boolean updateAccountStatus(Integer id, Boolean status) {
		Optional<UserDtls>  userFound =  userRepositoryObj.findById(id);
		
		if (userFound.isPresent()) {
			UserDtls userDtlObj = userFound.get();
			userDtlObj.setIsEnable(status);
			userRepositoryObj.save(userDtlObj);
			return true;
		}
		
		return false;
	}
	
	
}
