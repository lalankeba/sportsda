package com.laan.sportsda.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "faculty")
public class FacultyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @OneToMany(mappedBy = "facultyEntity")
    private List<DepartmentEntity> departmentEntities;

    @Column(name = "version")
    @Version
    private Long version;

}
