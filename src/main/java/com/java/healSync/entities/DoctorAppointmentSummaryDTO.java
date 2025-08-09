package com.java.healSync.entities;

public class DoctorAppointmentSummaryDTO {
    private Long doctorId;
    private String doctorName;
    private Long totalAppointmentsReceived;
    private Long confirmed;
    private Long pending;
    private Long cancelled;

    public DoctorAppointmentSummaryDTO(Long doctorId, String doctorName, Long totalAppointmentsReceived, Long confirmed, Long pending, Long cancelled) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.totalAppointmentsReceived = totalAppointmentsReceived;
        this.confirmed = confirmed;
        this.pending = pending;
        this.cancelled = cancelled;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    // Getters and setters
    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getTotalAppointmentsReceived() {
        return totalAppointmentsReceived;
    }

    public void setTotalAppointmentsReceived(Long totalAppointmentsReceived) {
        this.totalAppointmentsReceived = totalAppointmentsReceived;
    }

    public Long getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Long confirmed) {
        this.confirmed = confirmed;
    }

    public Long getPending() {
        return pending;
    }

    public void setPending(Long pending) {
        this.pending = pending;
    }

    public Long getCancelled() {
        return cancelled;
    }

    public void setCancelled(Long cancelled) {
        this.cancelled = cancelled;
    }
}
