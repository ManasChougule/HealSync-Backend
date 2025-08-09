package com.java.healSync.dataAccess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.healSync.entities.Patient;
import com.java.healSync.entities.User;

public interface PatientDao extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUser(User user);  // Finds the patient based on the user

    void deleteByUser(User user);
}
