package com.laan.sportsda.client.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CountryClientResponse {

    private CountryName name;
    private String cca2;
    private String cca3;
    private String ccn3;
    private List<String> capital;
    private Long area;
    private Long population;
}
