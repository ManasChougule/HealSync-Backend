package com.java.loginReg.dataAccess;

import com.java.loginReg.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // You can add custom queries here if needed
}
