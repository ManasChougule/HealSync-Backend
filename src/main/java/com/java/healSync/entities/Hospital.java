package com.java.healSync.entities;

import jakarta.persistence.*;

@Entity
public class Hospital {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String address;

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	private String city;

	// Getters and Setters

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
