package com.java.loginReg.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.loginReg.business.abstracts.AppointmentService;
import com.java.loginReg.dataAccess.AppointmentDao;
import com.java.loginReg.dataAccess.DoctorDao;
import com.java.loginReg.dataAccess.PatientDao;
import com.java.loginReg.entities.Appointment;
import com.java.loginReg.entities.Doctor;
import com.java.loginReg.entities.Patient;
import com.java.loginReg.entities.Status;

@Service
public class AppointmentManager implements AppointmentService {

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private PatientDao patientDao;

    // Method to create an appointment
    @Override
    public Appointment createAppointment(Long doctorId, Long patientId, String day, String time) {
        // Retrieve the relevant doctor and patient data
        Doctor doctor = doctorDao.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientDao.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));

        // Create a new appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDay(day);
        appointment.setTime(time);
        appointment.setStatus(Status.PENDING);

        // Save the appointment in the database
        return appointmentDao.save(appointment);
    }

    // Method to list all appointments
    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentDao.findAll();
    }

    // Method to delete an appointment
    @Override
    public boolean deleteAppointment(Long id) {
        if (appointmentDao.existsById(id)) { // If the appointment exists in the database
            appointmentDao.deleteById(id); // Delete the appointment
            return true;
        }
        return false;
    }

    // Method to list appointments by doctor ID
    @Override
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentDao.findByDoctorId(doctorId);
    }

    // Method to update the appointment status
    @Override
    public Appointment updateAppointmentStatus(Long appointmentId, Status status) {
        Appointment appointment = appointmentDao.findById(appointmentId).orElse(null);

        if (appointment != null) {
            appointment.setStatus(status); // Update the status
            return appointmentDao.save(appointment); // Save the updated appointment
        }

        return null;
    }

    // Method to list appointments by patient ID
    @Override
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentDao.findByPatientId(patientId);
    }

    // Method to check doctor's availability
    public boolean isDoctorAvailable(Long doctorId, String day, String time) {
        // Search the database for previously booked appointments for the specified doctor, day, and time
        List<Appointment> appointments = appointmentDao.findByDoctorIdAndDayAndTime(doctorId, day, time);

        // If there is an appointment, the doctor is not available at that time
        return appointments.isEmpty();
    }

    // Method to update an appointment
    @Override
    public Appointment updateAppointment(Long appointmentId, String day, String time) {
        Appointment appointment = appointmentDao.findById(appointmentId).orElse(null);
        if (appointment != null) {
            appointment.setDay(day); // Update the appointment day
            appointment.setTime(time); // Update the appointment time
            return appointmentDao.save(appointment); // Save the updated appointment
        }
        return null;
    }
}