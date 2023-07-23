package com.laan.sportsda.controller;

import com.laan.sportsda.dto.request.FeatureAddRequest;
import com.laan.sportsda.dto.response.FeatureResponse;
import com.laan.sportsda.service.FeatureService;
import com.laan.sportsda.util.PathUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathUtil.FEATURES)
@RequiredArgsConstructor
@Slf4j
public class FeatureController {

    private final FeatureService featureService;

    @GetMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> getFeature(@PathVariable("id") String id) {
        log.info("getting feature for id: {}", id);
        FeatureResponse featureResponse = featureService.getFeature(id);
        log.info("get feature {}", featureResponse);
        return new ResponseEntity<>(featureResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getFeatures() {
        log.info("getting features");
        List<FeatureResponse> featureResponses = featureService.getFeatures();
        log.info("get features");
        return new ResponseEntity<>(featureResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addFeature(@Valid @RequestBody FeatureAddRequest featureAddRequest) {
        log.info("adding feature");
        FeatureResponse featureResponse = featureService.addFeature(featureAddRequest);
        log.info("added new feature");
        return new ResponseEntity<>(featureResponse, HttpStatus.CREATED);
    }
}
