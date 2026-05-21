package com.ecom.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserDtls {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;
	private String mobileNumber;
	private String email;
	private String address;
	private String city;
	private String state;
	private String pincode;
	private String password;
	private String profileImage;
	private String role;

	private Boolean isEnable;
	private Boolean accountNotLocked;

	private Integer failedAttempt;

	private Date lockTime;

	private String resetToken;

	// All Getter Methods
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public String getEmail() {
		return this.email;
	}

	public String getAddress() {
		return this.address;
	}

	public String getCity() {
		return this.city;
	}

	public String getState() {
		return this.state;
	}

	public String getPincode() {
		return this.pincode;
	}

	public String getPassword() {
		return this.password;
	}

	public String getProfileImage() {
		return this.profileImage;
	}

	public String getRole() {
		return this.role;
	}

	public Boolean getIsEnable() {
		return this.isEnable;
	}

	public Boolean getAccountNonLocked() {
		return this.accountNotLocked;
	}

	public Integer getFailedAttempt() {
		return this.failedAttempt;
	}

	public Date getLockTime() {
		return this.lockTime;
	}

	public String getResetToken() {
		return this.resetToken;
	}

	// All Setter Methods
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setIsEnable(Boolean status) {
		this.isEnable = status;
	}

	public void setAccountNotLocked(Boolean accountNotLocked) {
		this.accountNotLocked = accountNotLocked;
	}

	public void setFailedAttempt(Integer failedAttempt) {
		this.failedAttempt = failedAttempt;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}
}
