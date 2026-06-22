package com.ecom.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderRequest {

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNo;

	private String address;

	private String city;

	private String state;

	private String pincode;

	private String paymentType;

	// All Getter Methods

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

	public String getPaymentType() {
		return this.paymentType;
	}

	// All Setter Methods

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

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	@Override
	public String toString() {
		return "OrderRequest{" 
				+ "firstName=" + firstName
				+ ", lastName=" + lastName
				+ ", email=" + email
				+ ", mobileNo=" + mobileNo
				+ ", address=" + address
				+ ", city=" + city
				+ ", state=" + state
				+ ", pincode=" + pincode
				+ ", payementType=" + paymentType
				+ "};";
	}
}
