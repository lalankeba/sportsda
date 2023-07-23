package com.laan.sportsda.repository;

import com.laan.sportsda.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {

    Optional<RoleEntity> findByName(String name);
    Optional<RoleEntity> findByNameAndIdNotContains(String name, String id);
}
