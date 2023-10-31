package com.laan.sportsda.repository;

import com.laan.sportsda.entity.FeatureEntity;
import com.laan.sportsda.entity.SportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeatureRepository extends JpaRepository<FeatureEntity, String> {

    Optional<FeatureEntity> findByNameAndSportEntity(String name, SportEntity sportEntity);
    Optional<FeatureEntity> findByNameAndSportEntityAndIdNotContains(String name, SportEntity sportEntity, String id);
}
