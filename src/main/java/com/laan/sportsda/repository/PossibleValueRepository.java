package com.laan.sportsda.repository;

import com.laan.sportsda.entity.FeatureEntity;
import com.laan.sportsda.entity.PossibleValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PossibleValueRepository extends JpaRepository<PossibleValueEntity, String> {

    List<PossibleValueEntity> findByFeatureEntity(FeatureEntity featureEntity);
}
