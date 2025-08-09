package com.java.healSync.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.healSync.entity.Patient;
import com.java.healSync.entity.User;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUser(User user);  // Finds the patient based on the user

    void deleteByUser(User user);
}
