package com.laan.sportsda.repository;

import com.laan.sportsda.entity.SportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SportRepository extends JpaRepository<SportEntity, String> {

    Optional<SportEntity> findByName(String name);

    Optional<SportEntity> findByNameAndIdNotContains(String name, String id);
}
