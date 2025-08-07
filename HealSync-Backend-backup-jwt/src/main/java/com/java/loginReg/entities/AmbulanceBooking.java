package com.java.loginReg.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AmbulanceBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pickupLocation;
    private String dropLocation;
    private Long patientId;

    private String patientName;
    private int age;

    private LocalDateTime bookingTime;

    @ManyToOne
    @JoinColumn(name = "ambulance_id")
    private Ambulance ambulance;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Ambulance getAmbulance() {
        return ambulance;
    }

    public void setAmbulance(Ambulance ambulance) {
        this.ambulance = ambulance;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
