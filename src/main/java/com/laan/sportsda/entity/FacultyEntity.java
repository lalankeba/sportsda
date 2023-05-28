package com.laan.sportsda.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "faculty")
public class FacultyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(name = "version")
    @Version
    private Long version;

}
