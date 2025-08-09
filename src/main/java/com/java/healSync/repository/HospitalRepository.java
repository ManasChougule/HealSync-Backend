package com.java.healSync.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.healSync.entity.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

	Optional<Hospital> findByName(String name);
}
