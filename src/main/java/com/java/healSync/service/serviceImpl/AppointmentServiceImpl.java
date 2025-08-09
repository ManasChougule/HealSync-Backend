package com.java.healSync.service.serviceImpl;

import java.util.*;

import com.java.healSync.dto.DayLoadDTO;
import com.java.healSync.dto.DoctorAppointmentSummaryDTO;
import com.java.healSync.dto.DoctorLoadResponseDTO;
import com.java.healSync.entity.*;
import com.java.healSync.enums.Status;
import com.java.healSync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.healSync.service.services.AppointmentService;
import com.java.healSync.repository.AppointmentRepository;
import com.java.healSync.repository.DoctorRepository;
import com.java.healSync.repository.PatientRepository;

import java.util.stream.Collectors;


@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentDao;

    @Autowired
    private DoctorRepository doctorDao;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    // Method to create an appointment
    @Override
    public Appointment createAppointment(Long doctorId, Long patientId, String day, String time) {
        // Retrieve the relevant doctor and patient data
        Doctor doctor = doctorDao.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));

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

    public List<String> getFilteredAppointmentsByDay(Long doctorId, String day) {
        // Search the database for previously booked appointments for the specified doctor and day
        List<Appointment> bookedAppointments = appointmentDao.findByDoctorIdAndDay(doctorId, day);
        List<String> bookedTimes = new ArrayList<>();
        for(int i=0;i<bookedAppointments.size();i++){
            bookedTimes.add(bookedAppointments.get(i).getTime());
        }

        Optional<Doctor> optionalUser = doctorDao.findById(doctorId);
        List<String> availableSlots = new ArrayList<>();
        if (optionalUser.isPresent()) {
            Doctor doctor = optionalUser.get();

            // fetch default working hours of doctor
            List<String> defaultWorkingHours = List.of(doctor.getWorkingHours().split(","));

            // remove booked slots from default working hours
            availableSlots = defaultWorkingHours.stream()
                    .filter(time -> !bookedTimes.contains(time))
                    .collect(Collectors.toList());
        }

        // If there is an appointment, the doctor is not available at that time
        return availableSlots;
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

    public List<DoctorAppointmentSummaryDTO> getDoctorAppointmentSummary() {
        return appointmentDao.getDoctorAppointmentSummary();
    }

    @Override
    public DoctorLoadResponseDTO getDoctorLoadById(Long doctorId) {
        List<Object[]> results = appointmentDao.getAppointmentStatusCountPerDay(doctorId);
        Doctor doctor = doctorDao.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));

        Map<String, DayLoadDTO> loadMap = new HashMap<>();

        for (Object[] row : results) {
            String day = (String) row[0];
            String status = row[1].toString();
            Long count = (Long) row[2];

            DayLoadDTO dayLoad = loadMap.getOrDefault(day, new DayLoadDTO());
            for (int i = 0; i < count; i++) {
                dayLoad.incrementStatus(status);
            }
            loadMap.put(day, dayLoad);
        }

        String doctorName = "Dr. " + doctor.getUser().getFirstName() + " " + doctor.getUser().getLastName();

        return new DoctorLoadResponseDTO(doctor.getId(), doctorName, loadMap);
    }


    public List<Appointment> patientAppointmentSummary(Long patientId) {
        return appointmentDao.findByPatientId(patientId);
    }

    public DoctorAppointmentSummaryDTO getPatientAppointmentSummary(Long userId) {
        // from user id fetch patient id
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = userOpt.get();

        if (user.getPatient() == null) {
            throw new RuntimeException("User with id " + userId + " is not a patient.");
        }

        Long patientId = user.getPatient().getId();
        List<Appointment> appointments = appointmentDao.findByPatientId(patientId);

        int confirmed = 0, cancelled = 0, pending = 0;

        for (Appointment appt : appointments) {
            switch (appt.getStatus()) {
                case CONFIRMED:
                    confirmed++;
                    break;
                case CANCELLED:
                    cancelled++;
                    break;
                case PENDING:
                    pending++;
                    break;
            }
        }

        int total = appointments.size();

        // Get patient name from Patient -> User
        if (appointments.isEmpty()) {
            throw new RuntimeException("No appointments found for patient id: " + patientId);
        }

        Patient patient = appointments.get(0).getPatient();
        String firstName = patient.getUser().getFirstName();
        String lastName = patient.getUser().getLastName();
        String fullName = firstName + " " + lastName;

        // Reusing DoctorAppointmentSummaryDTO for patient summary
        return new DoctorAppointmentSummaryDTO(
                patient.getId(),       // doctorId used as patientId here
                fullName,              // doctorName used as patientName
                (long) total,
                (long) confirmed,
                (long) pending,
                (long) cancelled
        );
    }


}