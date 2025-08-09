package com.java.healSync.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Patient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
//	@JsonBackReference
	@JsonIgnoreProperties({"patient", "doctor"})
	private User user;
	
	public Patient() {
		
	}

	public Patient(User user) {
		super();
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
