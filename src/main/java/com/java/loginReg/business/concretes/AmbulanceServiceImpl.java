// AmbulanceServiceImpl.java
package com.java.loginReg.business.concretes;

import com.java.loginReg.business.abstracts.AmbulanceService;
import com.java.loginReg.dataAccess.AmbulanceRepository;
import com.java.loginReg.entities.Ambulance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AmbulanceServiceImpl implements AmbulanceService {

    @Autowired
    private AmbulanceRepository ambulanceRepository;

    @Override
    public List<Ambulance> getAll() {
        return ambulanceRepository.findAll();
    }

    @Override
    public List<Ambulance> getAvailableAmbulances() {
        return ambulanceRepository.findByAvailableTrue();
    }

    @Override
    public List<Ambulance> getAvailableAmbulancesByType(String type) {
        return ambulanceRepository.findByAvailableTrueAndType(type);
    }

    @Override
    public Ambulance addAmbulance(Ambulance ambulance) {
        ambulance.setAvailable(true);
        return ambulanceRepository.save(ambulance);
    }

    @Override
    public String bookAmbulance(Long ambulanceId) {
        Ambulance ambulance = ambulanceRepository.findById(ambulanceId).orElse(null);
        if (ambulance == null) {
            return "Ambulance not found";
        }

        if (!ambulance.isAvailable()) {
            return "Ambulance already booked";
        }

        ambulance.setAvailable(false);
        ambulance.setBookedAt(LocalDateTime.now());
        ambulanceRepository.save(ambulance);
        return "Ambulance booked successfully";
    }

    // Release booked ambulances after 30 minutes
    @Scheduled(fixedRate = 300000) // Every 5 minutes (for testing)
    public void releaseAmbulances() {
        List<Ambulance> all = ambulanceRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Ambulance ambulance : all) {
            if (!ambulance.isAvailable() && ambulance.getBookedAt() != null) {
                if (ambulance.getBookedAt().plusMinutes(30).isBefore(now)) {
                    ambulance.setAvailable(true);
                    ambulance.setBookedAt(null);
                    ambulanceRepository.save(ambulance);
                }
            }
        }
    }
}
