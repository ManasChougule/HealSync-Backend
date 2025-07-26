package com.java.loginReg.entities;


import javax.persistence.*;

@Entity
@Table(name = "ambulance")
public class Ambulance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNumber;

    private String driverName;

    private String contactNumber;

    private String status; // AVAILABLE, BOOKED, OUT_OF_SERVICE

    public Long getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
