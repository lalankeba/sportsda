package com.laan.sportsda.service;

import com.laan.sportsda.dto.request.FeatureAddRequest;
import com.laan.sportsda.dto.response.FeatureResponse;

import java.util.List;

public interface FeatureService {

    FeatureResponse getFeature(final String id);
    List<FeatureResponse> getFeatures();
    FeatureResponse addFeature(final FeatureAddRequest featureAddRequest);
}
