package com.java.loginReg.dataAccess;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.loginReg.entities.Appointment;
import com.java.loginReg.entities.Doctor;
import com.java.loginReg.entities.Patient;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentDao extends JpaRepository<Appointment, Long> {

	 List<Appointment> findByDoctorId(Long doctorId);
	 
	 List<Appointment> findByPatientId(Long patientId);
	 
	 List<Appointment> findByPatient(Patient patient);
	 
	 List<Appointment> findByDoctor(Doctor doctor);

	// Method to retrieve the doctor's appointments for the selected day and time
	 List<Appointment> findByDoctorIdAndDayAndTime(Long doctorId, String day, String time);
	 
	 List<Appointment> findByDoctorIdAndDayIn(Long doctorId, List<String> days);

	 List<Appointment> findByDoctorIdAndDay(Long doctorId, String day);


}
