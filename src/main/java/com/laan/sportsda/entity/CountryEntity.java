package com.laan.sportsda.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "country")
public class CountryEntity {

    @Id
    private String cca2;

    private String cca3;

    private String ccn3;

    @Column(name = "common_name")
    private String commonName;

    @Column(name = "official_name")
    private String officialName;

    private String capitals;

    private Double area;

    private Long population;

    private String region;

    private String subregion;

    private String continents;

    @Column(name = "flag_png")
    private String flagPng;

    @Column(name = "flag_svg")
    private String flagSvg;

    @Column(name = "modified_date_time")
    private LocalDateTime modifiedDateTime;

}
