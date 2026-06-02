package com.ecom.service.impl;

import com.ecom.model.UserDtls;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;
import com.ecom.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.Date;
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
		user.setAccountNotLocked(true);
		user.setFailedAttempt(0);

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
		Optional<UserDtls> userFound = userRepositoryObj.findById(id);

		if (userFound.isPresent()) {
			UserDtls userDtlObj = userFound.get();
			userDtlObj.setIsEnable(status);
			userRepositoryObj.save(userDtlObj);
			return true;
		}

		return false;
	}

	@Override
	public void increaseFailedAttempt(UserDtls user) {
		int attempt = user.getFailedAttempt() + 1;
		user.setFailedAttempt(attempt);
		userRepositoryObj.save(user);
	}

	@Override
	public void userAccountLock(UserDtls user) {
		user.setAccountNotLocked(false);
		user.setLockTime(new Date());
		userRepositoryObj.save(user);
	}

	@Override
	public boolean unlockAccountTimeExpired(UserDtls user) {
		long lockTime = user.getLockTime().getTime();
		long unLockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;

		long currentTime = System.currentTimeMillis();

		if (unLockTime < currentTime) {
			user.setAccountNotLocked(true);
			user.setFailedAttempt(0);
			user.setLockTime(null);
			userRepositoryObj.save(user);
			return true;
		}

		return false;
	}

	@Override
	public void resetAttempt(int userId) {

	}

	@Override
	public void updateUserResetToken(String email, String resetToken) {
		UserDtls userFound = userRepositoryObj.findByEmail(email);
		userFound.setResetToken(resetToken);
		userRepositoryObj.save(userFound);
	}

	@Override
	public UserDtls getUserByToken(String token) {
		return userRepositoryObj.findByResetToken(token);
	}

	@Override
	public UserDtls updateUser(UserDtls user) {
		return userRepositoryObj.save(user);
	}
}
