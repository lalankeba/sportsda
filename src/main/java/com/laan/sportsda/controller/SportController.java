package com.laan.sportsda.controller;

import com.laan.sportsda.dto.request.SportAddRequest;
import com.laan.sportsda.dto.request.SportUpdateRequest;
import com.laan.sportsda.dto.response.SportResponse;
import com.laan.sportsda.dto.response.SportShortResponse;
import com.laan.sportsda.service.SportService;
import com.laan.sportsda.util.PathUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathUtil.SPORTS)
@RequiredArgsConstructor
@Slf4j
public class SportController {

    private final SportService sportService;

    @GetMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> getSport(@PathVariable("id") String id) {
        log.info("getting sport for id: {}", id);
        SportResponse sportResponse = sportService.getSport(id);
        log.info("get sport {}", sportResponse);
        return new ResponseEntity<>(sportResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getSports() {
        log.info("getting sports");
        List<SportShortResponse> sportShortResponses = sportService.getSports();
        log.info("get sports");
        return new ResponseEntity<>(sportShortResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addSport(@Valid @RequestBody SportAddRequest sportAddRequest) {
        log.info("adding sport");
        SportShortResponse sportShortResponse = sportService.addSport(sportAddRequest);
        log.info("added new sport");
        return new ResponseEntity<>(sportShortResponse, HttpStatus.CREATED);
    }

    @PutMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> updateSport(@PathVariable("id") String id, @Valid @RequestBody SportUpdateRequest sportUpdateRequest) {
        log.info("updating sport: {}", id);
        SportShortResponse sportShortResponse = sportService.updateSport(id, sportUpdateRequest);
        log.info("updated sport");
        return new ResponseEntity<>(sportShortResponse, HttpStatus.OK);
    }

    @DeleteMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> deleteSport(@PathVariable("id") String id) {
        log.info("deleting sport with id: {}", id);
        sportService.deleteSport(id);
        log.info("deleted sport with id: {}", id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
