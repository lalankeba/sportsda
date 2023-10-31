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
@Table(name = "sport")
public class SportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @OneToMany(mappedBy = "sportEntity", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<FeatureEntity> featureEntities;

    @Column(name = "version")
    @Version
    private Long version;

}
