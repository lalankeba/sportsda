package com.laan.sportsda.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sport")
public class SportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(name = "version")
    @Version
    private Long version;

}
