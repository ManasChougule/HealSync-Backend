package com.java.loginReg.dataAccess;


import com.java.loginReg.entities.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AmbulanceDao extends JpaRepository<Ambulance, Long> {
    List<Ambulance> findByStatus(String status);
}

