package com.java.loginReg.dataAccess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.loginReg.entities.Patient;
import com.java.loginReg.entities.User;

public interface PatientDao extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUser(User user);  // Finds the patient based on the user

    void deleteByUser(User user);
}
