package com.laan.sportsda.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryResponse {

    private String commonName;
    private String officialName;
    private String cca2;
    private String cca3;
    private String ccn3;
    private String capitals;
    private Long area;
    private Long population;
    private String region;
    private String subregion;
    private String continents;
    private String flagPng;
    private String flagSvg;

}
