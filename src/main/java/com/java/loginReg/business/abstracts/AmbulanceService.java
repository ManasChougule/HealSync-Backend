// AmbulanceService.java
package com.java.loginReg.business.abstracts;

import com.java.loginReg.entities.Ambulance;
import java.util.List;

public interface AmbulanceService {
    List<Ambulance> getAll();
    List<Ambulance> getAvailableAmbulances();
    List<Ambulance> getAvailableAmbulancesByType(String type);
    Ambulance addAmbulance(Ambulance ambulance);
    String bookAmbulance(Long ambulanceId);
}
