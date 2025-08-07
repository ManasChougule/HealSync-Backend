package com.java.loginReg.api.controllers;

import java.util.List;

import com.java.loginReg.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.loginReg.business.abstracts.AppointmentService;

@RestController
@RequestMapping("/appointments")
@CrossOrigin
public class AppointmentController {


    @Autowired
    private AppointmentService appointmentService;


    // The /create endpoint will create an appointment and return an Appointment object
    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDto appointmentRequest) {
        // Before creating an appointment, we check the doctor's availability
        boolean isAvailable = appointmentService.isDoctorAvailable(
                appointmentRequest.getDoctorId(),
                appointmentRequest.getDay(),
                appointmentRequest.getTime()
        );

        if (!isAvailable) {
            // If the doctor is not available, return an error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);  // Or return a suitable error message
        }

        // If the doctor is available, we create the appointment
        Appointment appointment = appointmentService.createAppointment(
                appointmentRequest.getDoctorId(),
                appointmentRequest.getPatientId(),
                appointmentRequest.getDay(),
                appointmentRequest.getTime()
        );

        // If the appointment is successfully created, return the Appointment object
        return ResponseEntity.ok(appointment);
    }


    // Endpoint to list appointments by doctor ID
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }

    // Endpoint to update appointment status
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

    // Endpoint to list appointments by patient ID
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> xgetAppointmentsByPatientId(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        return ResponseEntity.ok(appointments);
    }

    // Endpoint to check if a doctor is available on a specific day and time
    @PostMapping("/check-availability")
    public ResponseEntity<String> checkAvailability(@RequestBody AppointmentDto appointment) {

        boolean isAvailable = appointmentService.isDoctorAvailable(appointment.getDoctorId(), appointment.getDay(), appointment.getTime());

        if (isAvailable) {
            return ResponseEntity.ok("Doctor is available at this time.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Doctor is not available at this time.");
        }
    }

    // Endpoint to update an appointment
    @PutMapping("/update/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long appointmentId, @RequestParam String day, String time) {
        Appointment updatedAppointment = appointmentService.updateAppointment(appointmentId, day, time);
        if (updatedAppointment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedAppointment);
    }

    @PostMapping("/get-filtered-time")
    public ResponseEntity<List<String>> getFilteredTimeByDay(@RequestBody AppointmentDto appointment ){
        List<String> days = appointmentService.getFilteredAppointmentsByDay(appointment.getDoctorId(), appointment.getDay());
        return ResponseEntity.ok(days);
    }

    @GetMapping("/doctors-availability-summary")
    public List<DoctorAppointmentSummaryDTO> getDoctorAppointmentSummary() {
        return appointmentService.getDoctorAppointmentSummary();
    }

    @GetMapping("/doctor-load/{doctorId}")
    public ResponseEntity<DoctorLoadResponseDTO> getDoctorLoad(@PathVariable Long doctorId) {
        DoctorLoadResponseDTO response = appointmentService.getDoctorLoadById(doctorId);
        return ResponseEntity.ok(response);
    }

}