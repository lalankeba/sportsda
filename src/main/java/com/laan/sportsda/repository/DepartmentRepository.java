package com.laan.sportsda.repository;

import com.laan.sportsda.entity.DepartmentEntity;
import com.laan.sportsda.entity.FacultyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, String> {

    Optional<DepartmentEntity> findByName(String name);

    Optional<DepartmentEntity> findByNameAndIdNotContains(String name, String id);

    List<DepartmentEntity> findByFacultyEntity(FacultyEntity facultyEntity);
}
