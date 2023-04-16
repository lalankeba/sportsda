package com.laan.sportsda.repository;

import com.laan.sportsda.entity.FacultyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<FacultyEntity, String> {

    Optional<FacultyEntity> findByName(String name);

    Optional<FacultyEntity> findByNameAndIdNotContains(String name, String id);
}
