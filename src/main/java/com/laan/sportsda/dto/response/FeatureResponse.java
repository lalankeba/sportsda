package com.laan.sportsda.dto.response;

import com.laan.sportsda.enums.FeatureValueType;
import lombok.Data;

import java.util.List;

@Data
public class FeatureResponse {

    private String id;

    private String name;

    private FeatureValueType featureValueType;

    private String fromValue;

    private String toValue;

    private String measurement;

    private String sportId;

    private List<PossibleValueResponse> possibleValues;

    private Long version;
}
