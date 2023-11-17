package com.laan.sportsda.repository;

import com.laan.sportsda.entity.PermissionEntity;
import com.laan.sportsda.enums.PermissionDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {

    Optional<PermissionEntity> findByDescription(PermissionDescription description);

    @Query("SELECT p.id FROM PermissionEntity p")
    List<String> getAllIds();

    @Transactional
    @Modifying
    @Query("DELETE FROM PermissionEntity p WHERE p.id = :id")
    void deleteByPermissionId(String id);
}
