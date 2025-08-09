// AmbulanceService.java
package com.java.healSync.business.abstracts;

import com.java.healSync.entities.Ambulance;
import java.util.List;

public interface AmbulanceService {
    List<Ambulance> getAll();
    List<Ambulance> getAvailableAmbulances();
    List<Ambulance> getAvailableAmbulancesByType(String type);
    Ambulance addAmbulance(Ambulance ambulance);
    String bookAmbulance(Long ambulanceId);
}
