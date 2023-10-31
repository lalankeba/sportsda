package com.laan.sportsda.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SportResponse {

    private String id;

    private String name;

    private List<FeatureResponse> features;

    private Long version;
}
