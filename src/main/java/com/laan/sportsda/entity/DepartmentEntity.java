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
@Table(name = "department")
public class DepartmentEntity extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    @ToString.Exclude
    private FacultyEntity facultyEntity;

    @ManyToMany(mappedBy = "departmentEntities")
    @ToString.Exclude
    private List<MemberEntity> memberEntities;

}
