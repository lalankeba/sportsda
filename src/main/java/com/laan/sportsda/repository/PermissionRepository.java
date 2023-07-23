package com.laan.sportsda.repository;

import com.laan.sportsda.entity.PermissionEntity;
import com.laan.sportsda.enums.PermissionDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {

    Optional<PermissionEntity> findByDescription(PermissionDescription description);
}
