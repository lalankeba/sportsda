package com.laan.sportsda.entity;

import com.laan.sportsda.enums.FeatureValueType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
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

    @Column(name = "min_value")
    private String minValue;

    @Column(name = "max_value")
    private String maxValue;

    private String measurement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    @ToString.Exclude
    private SportEntity sportEntity;

    @OneToMany(mappedBy = "featureEntity", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PossibleValueEntity> possibleValueEntities;

    @OneToMany(mappedBy = "featureEntity")
    @ToString.Exclude
    private List<MemberFeatureEntity> memberFeatureEntities;

    @Column(name = "version")
    @Version
    private Long version;

}
