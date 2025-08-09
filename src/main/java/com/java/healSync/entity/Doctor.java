package com.java.healSync.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specialization_id", referencedColumnName = "id")
	@JsonManagedReference 
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Specialization specialization;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospital_id", referencedColumnName = "id")
	@JsonManagedReference 
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Hospital hospital;
	
	@Column(name = "working_days")
	private String workingDays;
	
	@Column(name = "working_hours")
	private String workingHours;
	
	public Doctor(User user, Specialization specialization, Hospital hospital, String workingDays, String workingHours) {
		super();
		this.user = user;
		this.specialization = specialization;
		this.hospital = hospital;
		this.workingDays = workingDays;
		this.workingHours = workingHours;
	}

	public Doctor() {
		super();
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Specialization getSpecialization() {
		return specialization;
	}

	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public String getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(String workingDays) {
		this.workingDays = workingDays;
	}

	public String getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(String workingHours) {
		this.workingHours = workingHours;
	}

}
