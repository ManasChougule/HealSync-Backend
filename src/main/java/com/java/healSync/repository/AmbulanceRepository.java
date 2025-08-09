// AmbulanceRepository.java
package com.java.healSync.repository;

import com.java.healSync.entity.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AmbulanceRepository extends JpaRepository<Ambulance, Long> {
    List<Ambulance> findByAvailableTrue();
    List<Ambulance> findByAvailableTrueAndType(String type);
    List<Ambulance> findByTypeAndAvailableTrue(String type);

}
