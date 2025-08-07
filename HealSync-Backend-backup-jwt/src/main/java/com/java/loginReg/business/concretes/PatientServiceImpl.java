package com.java.loginReg.business.concretes;

import com.java.loginReg.business.abstracts.PatientService;
import com.java.loginReg.dataAccess.PatientRepository;
import com.java.loginReg.entities.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        Optional<Patient> optional = patientRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
