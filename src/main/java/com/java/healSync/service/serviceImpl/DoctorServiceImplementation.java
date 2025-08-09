package com.java.healSync.service.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.healSync.service.services.DoctorService;
import com.java.healSync.repository.AppointmentRepository;
import com.java.healSync.repository.DoctorRepository;
import com.java.healSync.repository.HospitalRepository;
import com.java.healSync.repository.SpecializationRepository;
import com.java.healSync.entity.Appointment;
import com.java.healSync.entity.Doctor;
import com.java.healSync.dto.DoctorDto;
import com.java.healSync.entity.Specialization;
import com.java.healSync.enums.Status;
import com.java.healSync.entity.Hospital;

@Service
public class DoctorServiceImplementation implements DoctorService{

    @Autowired
    private DoctorRepository doctorDao;

    @Autowired
    private HospitalRepository hospitalDao;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private AppointmentRepository appointmentDao;

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorDao.findAll();
    }

    @Override
    public Doctor save(Doctor doctor) {
        return doctorDao.save(doctor);
    }

    // Find doctor by ID
    public Doctor getDoctorById(Long id) {
        return doctorDao.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    // Retrieve doctors by specialization
    public List<Doctor> findBySpecialization(Specialization specialization) {
        return doctorDao.findBySpecialization(specialization); // Fetch data from the repository
    }

    // Update doctor information
    public boolean updateDoctor(Long Id, DoctorDto doctorDto) {
        if (doctorDao.existsById(Id)) {
            Doctor existingDoctor = (Doctor) doctorDao.findById(Id)
                    .orElseThrow(() -> new RuntimeException("Doctor with given ID not found"));

            // Update hospital information
            Hospital hospital = hospitalDao.findByName(doctorDto.getHospital())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid hospital name: " + doctorDto.getHospital()));
            existingDoctor.setHospital(hospital);

            // Update specialization information
            Specialization specialization = specializationRepository.findByName(doctorDto.getSpecialization())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid specialization name: " + doctorDto.getSpecialization()));
            existingDoctor.setSpecialization(specialization);

            // Update working days
            String oldWorkingDays = existingDoctor.getWorkingDays();
            String newWorkingDays = doctorDto.getWorkingDays();
            existingDoctor.setWorkingDays(newWorkingDays);

            // Cancel appointments based on changes in working days
            List<String> removedDays = oldWorkingDays!=null ? Arrays.stream(oldWorkingDays.split(","))
                    .filter(day -> !newWorkingDays.contains(day))
                    .toList() : new ArrayList<>();

            // Update working hours
            String newWorkingHours = doctorDto.getWorkingHours();
            existingDoctor.setWorkingHours(newWorkingHours);


            List<Appointment> appointmentsToCancel = appointmentDao.findByDoctorIdAndDayIn(existingDoctor.getId(), removedDays);
            appointmentsToCancel.forEach(appointment -> {
                appointment.setStatus(Status.CANCELLED);
                appointmentDao.save(appointment);
            });

            doctorDao.save(existingDoctor);
            return true;
        }
        return false;
    }
}