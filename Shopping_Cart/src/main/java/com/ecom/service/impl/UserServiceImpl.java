package com.ecom.service.impl;

import com.ecom.model.UserDtls;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;
import com.ecom.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

	@Override
	public UserDtls updateUserProfile(UserDtls user, MultipartFile imageFile) {

		UserDtls currentUser = userRepositoryObj.findById(user.getId()).get();

		if (!ObjectUtils.isEmpty(currentUser)) {
			currentUser.setName(user.getName());
			currentUser.setMobileNumber(user.getMobileNumber());
			currentUser.setAddress(user.getAddress());
			currentUser.setCity(user.getCity());
			currentUser.setState(user.getState());
			currentUser.setPincode(user.getPincode());

			try {
				if (!imageFile.isEmpty()) {

					currentUser.setProfileImage(imageFile.getOriginalFilename());

					/*
					 * File saveFile = new ClassPathResource("static/img").getFile();
					 * 
					 * Path path = Paths.get(saveFile.getAbsolutePath() + File.separator +
					 * "profile_img" + File.separator + file.getOriginalFilename());
					 * 
					 * // System.out.println(path); Files.copy(file.getInputStream(), path,
					 * StandardCopyOption.REPLACE_EXISTING);
					 */

					// Save image to folder
					String uploadDir = "C:/Users/herry/Desktop/STS_Workspace/Shopping_Cart/src/main/resources/static/img/profile_img/";
					// "C:\\Users\\herry\\Desktop\\STS_Workspace\\Shopping_Cart\\src\\main\\resources\\static\\img\\category_img"

					File dir = new File(uploadDir);
					if (!dir.exists())
						dir.mkdirs();

					String imageName = imageFile.getOriginalFilename();
					Path filePath = Paths.get(uploadDir + imageName);

					Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				}

			} catch (IOException e) {
				e.printStackTrace();

				return null;
			}

			currentUser = userRepositoryObj.save(currentUser);
		}

		return currentUser;
	}

	public Page<UserDtls> getUsers(String role, Integer pageNumber, Integer pageSize) {

		Pageable pageableObj = PageRequest.of(pageNumber, pageSize);
		Page<UserDtls> pageOrder = null;

		pageOrder = userRepositoryObj.findByRole(role, pageableObj);

		return pageOrder;
	}

	@Override
	public UserDtls saveAdminUser(UserDtls adminUser) {
		adminUser.setRole("ROLE_ADMIN");
		adminUser.setIsEnable(true);
		adminUser.setAccountNotLocked(true);
		adminUser.setFailedAttempt(0);

		String encodedPassword = passwordEncoder.encode(adminUser.getPassword());
		adminUser.setPassword(encodedPassword);

		UserDtls savedAdminUser = userRepositoryObj.save(adminUser);
		return savedAdminUser;
	}
}
