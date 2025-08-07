package com.java.loginReg.dataAccess;

import java.util.List;


import com.java.loginReg.entities.DoctorAppointmentSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.java.loginReg.entities.Appointment;
import com.java.loginReg.entities.Doctor;
import com.java.loginReg.entities.Patient;
import org.springframework.data.jpa.repository.Query;
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

	@Query("SELECT new com.java.loginReg.entities.DoctorAppointmentSummaryDTO(" +
			"a.doctor.id, " +
			"CONCAT(a.doctor.user.firstName, ' ', a.doctor.user.lastName), " +
			"COUNT(a), " +
			"SUM(CASE WHEN a.status = com.java.loginReg.entities.Status.CONFIRMED THEN 1 ELSE 0 END), " +
			"SUM(CASE WHEN a.status = com.java.loginReg.entities.Status.PENDING THEN 1 ELSE 0 END), " +
			"SUM(CASE WHEN a.status = com.java.loginReg.entities.Status.CANCELLED THEN 1 ELSE 0 END)) " +
			"FROM Appointment a GROUP BY a.doctor.id, a.doctor.user.firstName, a.doctor.user.lastName")
	List<DoctorAppointmentSummaryDTO> getDoctorAppointmentSummary();

}
