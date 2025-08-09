package com.java.healSync.dataAccess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.healSync.entities.Hospital;

public interface HospitalDao extends JpaRepository<Hospital, Long> {

	Optional<Hospital> findByName(String name);
}
