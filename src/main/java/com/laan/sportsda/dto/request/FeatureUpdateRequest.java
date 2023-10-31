package com.laan.sportsda.dto.request;

import com.laan.sportsda.enums.FeatureValueType;
import com.laan.sportsda.util.MessagesUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class FeatureUpdateRequest {

    @NotNull(message = MessagesUtil.AnnotationSupported.MANDATORY_FEATURE_NAME)
    @Size(min = 2, max = 150, message = MessagesUtil.AnnotationSupported.INVALID_FEATURE_NAME_SIZE)
    private String name;

    @NotNull
    private FeatureValueType featureValueType;

    private String minValue;
    private String maxValue;
    private String measurement;
    private List<String> possibleValues;

    @NotNull(message = MessagesUtil.AnnotationSupported.MANDATORY_VERSION)
    private Long version;

}
