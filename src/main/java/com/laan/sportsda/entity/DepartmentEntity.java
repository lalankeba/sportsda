package com.laan.sportsda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "department")
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    private FacultyEntity facultyEntity;

    @ManyToMany(mappedBy = "departmentEntities")
    @ToString.Exclude
    private List<MemberEntity> memberEntities;

    @Column(name = "version")
    @Version
    private Long version;
}
