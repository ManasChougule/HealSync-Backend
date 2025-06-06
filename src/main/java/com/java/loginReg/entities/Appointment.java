package com.java.loginReg.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	 
	@ManyToOne
	private Doctor doctor; // A doctor can have multiple appointments

	@ManyToOne
	private Patient patient; // A patient can have multiple appointments
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status; // Appointment status: "pending", "confirmed", etc.
	
	@Column(name = "day")
    private String day;  // Selected day

    @Column(name = "time")
    private String time; // Selected time
   
	 
	public Appointment(Doctor doctor, Patient patient, Status status, String day, String time) {
		super();
		this.doctor = doctor;
		this.patient = patient;
		this.status = status;
		this.day = day;
		this.time = time;
	}

	public Appointment() {
		 super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
