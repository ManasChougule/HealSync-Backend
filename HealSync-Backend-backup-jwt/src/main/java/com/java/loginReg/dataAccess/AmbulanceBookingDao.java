package com.java.loginReg.dataAccess;


import com.java.loginReg.entities.AmbulanceBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AmbulanceBookingDao extends JpaRepository<AmbulanceBooking, Long> {
    //List<AmbulanceBooking> findByPatientId(Long patientId);
}
