package com.java.loginReg.business.abstracts;

import java.util.List;
import com.java.loginReg.entities.Appointment;
import com.java.loginReg.entities.Status;

public interface AppointmentService {
    Appointment createAppointment(Long doctorId, Long patientId, String day, String time);
    List<Appointment> getAllAppointments(); // THIS method must be here
    List<Appointment> getAppointmentsByDoctorId(Long doctorId);
    List<Appointment> getAppointmentsByPatientId(Long patientId);
    Appointment updateAppointmentStatus(Long appointmentId, Status status);
    boolean deleteAppointment(Long id);
    boolean isDoctorAvailable(Long doctorId, String day, String time);
    List<String> getFilteredAppointmentsByDay(Long doctorId, String day);
    Appointment updateAppointment(Long appointmentId, String day, String time);
}
