package com.java.healSync.service.services;

import java.util.List;

import com.java.healSync.entity.Appointment;
import com.java.healSync.dto.DoctorAppointmentSummaryDTO;
import com.java.healSync.dto.DoctorLoadResponseDTO;
import com.java.healSync.enums.Status;

public interface AppointmentService {
	
	Appointment createAppointment(Long doctorId, Long patientId, String day, String time);
	
	List<Appointment> getAllAppointments();
	
	boolean deleteAppointment(Long id);
	
	List<Appointment> getAppointmentsByDoctorId(Long doctorId);
	
	Appointment updateAppointmentStatus(Long appointmentId, Status status);
	
	List<Appointment> getAppointmentsByPatientId(Long patientId);

	boolean isDoctorAvailable(Long doctorId, String day, String time);
	
	Appointment updateAppointment(Long appointmentId, String day, String time);

	List<String> getFilteredAppointmentsByDay(Long doctorId, String day);

	List<DoctorAppointmentSummaryDTO> getDoctorAppointmentSummary();

	DoctorLoadResponseDTO getDoctorLoadById(Long doctorId);

	DoctorAppointmentSummaryDTO getPatientAppointmentSummary(Long userId);
}
