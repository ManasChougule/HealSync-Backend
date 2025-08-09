// AmbulanceService.java
package com.java.healSync.service.services;

import com.java.healSync.entity.Ambulance;
import java.util.List;

public interface AmbulanceService {
    List<Ambulance> getAll();
    List<Ambulance> getAvailableAmbulances();
    List<Ambulance> getAvailableAmbulancesByType(String type);
    Ambulance addAmbulance(Ambulance ambulance);
    String bookAmbulance(Long ambulanceId);
}
