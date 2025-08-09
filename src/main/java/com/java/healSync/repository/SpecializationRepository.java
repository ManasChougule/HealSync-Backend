package com.java.healSync.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.healSync.entity.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

	Optional<Specialization> findByName(String name);
}
