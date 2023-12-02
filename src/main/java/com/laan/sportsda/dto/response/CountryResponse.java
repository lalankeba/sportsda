package com.laan.sportsda.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CountryResponse {

    private String commonName;
    private String officialName;
    private String cca2;
    private String cca3;
    private String ccn3;
    private List<String> capital;
    private Long area;
    private Long population;

}
