package com.java.healSync.dataAccess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.healSync.entities.Specialization;

public interface SpecializationDao extends JpaRepository<Specialization, Long> {

	Optional<Specialization> findByName(String name);
}
