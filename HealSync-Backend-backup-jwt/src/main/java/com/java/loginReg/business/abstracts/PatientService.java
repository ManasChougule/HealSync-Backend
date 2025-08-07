package com.java.loginReg.business.abstracts;

import com.java.loginReg.entities.Patient;
import java.util.List;

public interface PatientService {
    List<Patient> getAllPatients();
    Patient getPatientById(Long id);
    Patient savePatient(Patient patient);
    void deletePatient(Long id);
}
