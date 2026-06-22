package com.ecom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class OrderAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNo;

	private String address;

	private String city;

	private String state;

	private String pincode;

	// All Getter Methods

	public Integer getId() {
		return this.id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public String getMobileNo() {
		return this.mobileNo;
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

	// All Setter Methods

	public void setId(Integer id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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

}
