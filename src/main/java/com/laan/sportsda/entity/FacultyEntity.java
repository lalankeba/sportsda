package com.laan.sportsda.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "faculty")
public class FacultyEntity extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @OneToMany(mappedBy = "facultyEntity")
    @ToString.Exclude
    private List<DepartmentEntity> departmentEntities;

    @Column(name = "version")
    @Version
    private Long version;

}
