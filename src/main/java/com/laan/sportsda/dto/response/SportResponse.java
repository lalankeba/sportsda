package com.laan.sportsda.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SportResponse {

    private String id;

    private String name;

    private List<FeatureResponse> features;

    private Long version;
}
