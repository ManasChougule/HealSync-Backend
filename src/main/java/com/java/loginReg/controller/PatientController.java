package com.java.loginReg.controller;

import com.java.loginReg.business.abstracts.PatientService;
import com.java.loginReg.dataAccess.UserRepository;
import com.java.loginReg.entities.Patient;
import com.java.loginReg.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserRepository userRepository;

    // ✅ PATIENT only
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    // ✅ PATIENT or ADMIN can view a specific patient
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    // ✅ PATIENT only can create their own patient profile
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient, Principal principal) {
        // Get logged-in user email from token
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        // Link user to patient
        patient.setUser(user);

        Patient savedPatient = patientService.savePatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

    // ✅ Only ADMIN can delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}
