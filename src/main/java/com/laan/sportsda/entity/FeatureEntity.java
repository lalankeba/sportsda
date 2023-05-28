package com.laan.sportsda.entity;

import com.laan.sportsda.enums.FeatureValueType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "feature")
public class FeatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(name = "feature_value_type")
    @Enumerated(EnumType.STRING)
    private FeatureValueType featureValueType;

    @Column(name = "from_value")
    private String fromValue;

    @Column(name = "to_value")
    private String toValue;

    private String measurement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    private SportEntity sportEntity;

    @Column(name = "version")
    @Version
    private Long version;
}
