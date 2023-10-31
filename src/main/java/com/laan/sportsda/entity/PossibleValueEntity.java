package com.laan.sportsda.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "possible_value")
public class PossibleValueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "attribute_value")
    private String attributeValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id")
    private FeatureEntity featureEntity;

}
