package com.ecom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
//@Getter
//@Setter
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String imageName;
	private Boolean isActive;
	
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getImageName() {
		return this.imageName;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
