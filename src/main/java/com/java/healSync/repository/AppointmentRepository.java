package com.java.healSync.repository;

import java.util.List;


import com.java.healSync.dto.DoctorAppointmentSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.java.healSync.entity.Appointment;
import com.java.healSync.entity.Doctor;
import com.java.healSync.entity.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	 List<Appointment> findByDoctorId(Long doctorId);
	 
	 List<Appointment> findByPatientId(Long patientId);
	 
	 List<Appointment> findByPatient(Patient patient);
	 
	 List<Appointment> findByDoctor(Doctor doctor);

	// Method to retrieve the doctor's appointments for the selected day and time
	 List<Appointment> findByDoctorIdAndDayAndTime(Long doctorId, String day, String time);
	 
	 List<Appointment> findByDoctorIdAndDayIn(Long doctorId, List<String> days);

	 List<Appointment> findByDoctorIdAndDay(Long doctorId, String day);

	@Query("SELECT new com.java.healSync.dto.DoctorAppointmentSummaryDTO(" +
			"a.doctor.id, " +
			"CONCAT(a.doctor.user.firstName, ' ', a.doctor.user.lastName), " +
			"COUNT(a), " +
			"SUM(CASE WHEN a.status = com.java.healSync.enums.Status.CONFIRMED THEN 1 ELSE 0 END), " +
			"SUM(CASE WHEN a.status = com.java.healSync.enums.Status.PENDING THEN 1 ELSE 0 END), " +
			"SUM(CASE WHEN a.status = com.java.healSync.enums.Status.CANCELLED THEN 1 ELSE 0 END)) " +
			"FROM Appointment a GROUP BY a.doctor.id, a.doctor.user.firstName, a.doctor.user.lastName")
	List<DoctorAppointmentSummaryDTO> getDoctorAppointmentSummary();


	@Query("SELECT a.day, a.status, COUNT(a) " +
			"FROM Appointment a " +
			"WHERE a.doctor.id = :doctorId " +
			"GROUP BY a.day, a.status")
	List<Object[]> getAppointmentStatusCountPerDay(Long doctorId);


}
