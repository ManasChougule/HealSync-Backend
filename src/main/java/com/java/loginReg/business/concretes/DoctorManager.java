package com.java.loginReg.business.concretes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.java.loginReg.business.abstracts.DoctorService;
import com.java.loginReg.dataAccess.*;
import com.java.loginReg.entities.*;
import com.java.loginReg.security.SecurityUtil;

@Service
public class DoctorManager implements DoctorService {

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private HospitalDao hospitalDao;

    @Autowired
    private SpecializationDao specializationDao;

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<Doctor> getAllDoctors() {
        return doctorDao.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Doctor save(Doctor doctor) {
        return doctorDao.save(doctor);
    }

    // Doctor or Admin can view doctor by ID
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Doctor getDoctorById(Long id) {
        return doctorDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    // Public or patient can see doctors by specialization (no need to restrict)
    public List<Doctor> findBySpecialization(Specialization specialization) {
        return doctorDao.findBySpecialization(specialization);
    }

    // ✅ DOCTOR can update *only their* profile
    @PreAuthorize("hasRole('DOCTOR')")
    public boolean updateDoctor(Long userId, DoctorDto doctorDto) {
        User currentUser = SecurityUtil.getCurrentUser(userRepository);

        // Block update if current doctor tries to edit someone else's profile
        if (!currentUser.getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to update this doctor's profile.");
        }

        if (doctorDao.existsByUserId(userId)) {
            Doctor existingDoctor = doctorDao.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Doctor with given ID not found"));

            // Hospital
            Hospital hospital = hospitalDao.findByName(doctorDto.getHospital())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid hospital name: " + doctorDto.getHospital()));
            existingDoctor.setHospital(hospital);

            // Specialization
            Specialization specialization = specializationDao.findByName(doctorDto.getSpecialization())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid specialization name: " + doctorDto.getSpecialization()));
            existingDoctor.setSpecialization(specialization);

            // Working days update
            String oldWorkingDays = existingDoctor.getWorkingDays();
            String newWorkingDays = doctorDto.getWorkingDays();
            existingDoctor.setWorkingDays(newWorkingDays);

            List<String> removedDays = oldWorkingDays != null
                    ? Arrays.stream(oldWorkingDays.split(","))
                        .filter(day -> !newWorkingDays.contains(day))
                        .toList()
                    : new ArrayList<>();

            // Working hours update
            existingDoctor.setWorkingHours(doctorDto.getWorkingHours());

            // Cancel appointments for removed days
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
