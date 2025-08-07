package com.java.loginReg.business.concretes;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.java.loginReg.business.abstracts.AppointmentService;
import com.java.loginReg.dataAccess.AppointmentDao;
import com.java.loginReg.dataAccess.DoctorDao;
import com.java.loginReg.dataAccess.PatientDao;
import com.java.loginReg.dataAccess.UserRepository;
import com.java.loginReg.entities.*;

@Service
public class AppointmentManager implements AppointmentService {

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private UserRepository userRepository;

    // 🔐 Helper to get current user
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("User not found"));
    }

    // ✅ Create Appointment
    @Override
    public Appointment createAppointment(Long doctorId, Long patientId, String day, String time) {
        Doctor doctor = doctorDao.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientDao.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDay(day);
        appointment.setTime(time);
        appointment.setStatus(Status.PENDING);

        return appointmentDao.save(appointment);
    }

    // ✅ Admin only
    @Override
    public List<Appointment> getAllAppointments() {
        User current = getCurrentUser();
        if (!current.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin can view all appointments");
        }
        return appointmentDao.findAll();
    }

    // ✅ Delete by Admin or Patient who owns it
    @Override
    public boolean deleteAppointment(Long id) {
        Appointment appt = appointmentDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        User current = getCurrentUser();
        boolean isAdmin = current.getRole().equals("ADMIN");
        boolean isOwner = appt.getPatient().getUser().getId().equals(current.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You are not allowed to delete this appointment");
        }

        appointmentDao.deleteById(id);
        return true;
    }

    // ✅ Only doctor can view their appointments
    @Override
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        User current = getCurrentUser();
        if (!current.getRole().equals("DOCTOR") || !current.getId().equals(doctorId)) {
            throw new AccessDeniedException("Access denied to doctor appointments");
        }
        return appointmentDao.findByDoctorId(doctorId);
    }

    // ✅ Only patient can view their appointments
    @Override
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        User current = getCurrentUser();
        if (!current.getRole().equals("PATIENT") || !current.getId().equals(patientId)) {
            throw new AccessDeniedException("Access denied to patient appointments");
        }
        return appointmentDao.findByPatientId(patientId);
    }

    // ✅ Only assigned doctor can update status
    @Override
    public Appointment updateAppointmentStatus(Long appointmentId, Status status) {
        Appointment appointment = appointmentDao.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        User current = getCurrentUser();
        if (!current.getRole().equals("DOCTOR") ||
            !appointment.getDoctor().getUser().getId().equals(current.getId())) {
            throw new AccessDeniedException("You are not allowed to update this appointment");
        }

        appointment.setStatus(status);
        return appointmentDao.save(appointment);
    }

    // 🔓 No role check needed (used internally for filtering availability)
    public boolean isDoctorAvailable(Long doctorId, String day, String time) {
        List<Appointment> appointments = appointmentDao.findByDoctorIdAndDayAndTime(doctorId, day, time);
        return appointments.isEmpty();
    }

    // 🔓 Slot filtering for availability (used during scheduling)
    public List<String> getFilteredAppointmentsByDay(Long doctorId, String day) {
        List<Appointment> bookedAppointments = appointmentDao.findByDoctorIdAndDay(doctorId, day);
        List<String> bookedTimes = bookedAppointments.stream()
                .map(Appointment::getTime)
                .collect(Collectors.toList());

        Optional<Doctor> optionalUser = doctorDao.findById(doctorId);
        List<String> availableSlots = new ArrayList<>();
        if (optionalUser.isPresent()) {
            Doctor doctor = optionalUser.get();
            List<String> defaultWorkingHours = List.of(doctor.getWorkingHours().split(","));
            availableSlots = defaultWorkingHours.stream()
                    .filter(time -> !bookedTimes.contains(time))
                    .collect(Collectors.toList());
        }
        return availableSlots;
    }

    // ✅ Only patient can update their own appointment
    @Override
    public Appointment updateAppointment(Long appointmentId, String day, String time) {
        Appointment appointment = appointmentDao.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        User current = getCurrentUser();
        if (!appointment.getPatient().getUser().getId().equals(current.getId())) {
            throw new AccessDeniedException("Not allowed to update this appointment");
        }

        appointment.setDay(day);
        appointment.setTime(time);
        return appointmentDao.save(appointment);
    }
}
