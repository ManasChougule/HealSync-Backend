package com.java.healSync.business.concretes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.healSync.business.abstracts.DoctorService;
import com.java.healSync.dataAccess.AppointmentDao;
import com.java.healSync.dataAccess.DoctorDao;
import com.java.healSync.dataAccess.HospitalDao;
import com.java.healSync.dataAccess.SpecializationDao;
import com.java.healSync.entities.Appointment;
import com.java.healSync.entities.Doctor;
import com.java.healSync.entities.DoctorDto;
import com.java.healSync.entities.Specialization;
import com.java.healSync.entities.Status;
import com.java.healSync.entities.Hospital;

@Service
public class DoctorManager implements DoctorService{

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private HospitalDao hospitalDao;

    @Autowired
    private SpecializationDao specializationDao;

    @Autowired
    private AppointmentDao appointmentDao;

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
            Specialization specialization = specializationDao.findByName(doctorDto.getSpecialization())
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