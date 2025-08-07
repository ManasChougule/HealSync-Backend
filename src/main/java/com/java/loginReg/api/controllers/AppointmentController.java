package com.java.loginReg.api.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.java.loginReg.business.abstracts.AppointmentService;
import com.java.loginReg.dataAccess.UserRepository;
import com.java.loginReg.entities.Appointment;
import com.java.loginReg.entities.AppointmentDto;
import com.java.loginReg.entities.Status;
import com.java.loginReg.entities.User;

@RestController
@RequestMapping("/appointments")
@CrossOrigin
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserRepository userRepository;

    // ✅ PATIENT can create appointment
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDto appointmentRequest, Principal principal) {
        String email = principal.getName();
        User patientUser = userRepository.findByEmail(email).orElse(null);
        if (patientUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        boolean isAvailable = appointmentService.isDoctorAvailable(
                appointmentRequest.getDoctorId(),
                appointmentRequest.getDay(),
                appointmentRequest.getTime()
        );

        if (!isAvailable) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Appointment appointment = appointmentService.createAppointment(
                appointmentRequest.getDoctorId(),
                patientUser.getId(),
                appointmentRequest.getDay(),
                appointmentRequest.getTime()
        );

        return ResponseEntity.ok(appointment);
    }

    // ✅ DOCTOR can see their appointments
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/doctor")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(Principal principal) {
        String email = principal.getName();
        User doctorUser = userRepository.findByEmail(email).orElse(null);
        if (doctorUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(doctorUser.getId());
        return ResponseEntity.ok(appointments);
    }

    // ✅ PATIENT can see their appointments
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientId(Principal principal) {
        String email = principal.getName();
        User patientUser = userRepository.findByEmail(email).orElse(null);
        if (patientUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(patientUser.getId());
        return ResponseEntity.ok(appointments);
    }

    // ✅ DOCTOR can update appointment status
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/update-status/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestParam Status status) {

        Appointment updatedAppointment = appointmentService.updateAppointmentStatus(appointmentId, status);

        if (updatedAppointment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedAppointment);
    }

    // ✅ Check availability – open to all authenticated users
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    @PostMapping("/check-availability")
    public ResponseEntity<String> checkAvailability(@RequestBody AppointmentDto appointment) {
        boolean isAvailable = appointmentService.isDoctorAvailable(
                appointment.getDoctorId(),
                appointment.getDay(),
                appointment.getTime());

        if (isAvailable) {
            return ResponseEntity.ok("Doctor is available at this time.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Doctor is not available at this time.");
        }
    }

    // ✅ PATIENT can update their own appointment (optional check by service)
    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/update/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long appointmentId,
                                                         @RequestParam String day,
                                                         @RequestParam String time) {
        Appointment updatedAppointment = appointmentService.updateAppointment(appointmentId, day, time);
        if (updatedAppointment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAppointment);
    }

    // ✅ PATIENT filters available times
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/get-filtered-time")
    public ResponseEntity<List<String>> getFilteredTimeByDay(@RequestBody AppointmentDto appointment) {
        List<String> days = appointmentService.getFilteredAppointmentsByDay(appointment.getDoctorId(), appointment.getDay());
        return ResponseEntity.ok(days);
    }
 // ✅ ADMIN can view all appointments
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }


}
