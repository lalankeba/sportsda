package com.laan.sportsda.controller;

import com.laan.sportsda.dto.response.CountryResponse;
import com.laan.sportsda.service.CountryService;
import com.laan.sportsda.util.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(PathUtil.COUNTRIES)
@RequiredArgsConstructor
@Slf4j
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<Object> getCountries() {
        log.info("getting countries");
        List<CountryResponse> countryResponses = countryService.getCountries();
        log.info("get countries");
        return new ResponseEntity<>(countryResponses, HttpStatus.OK);
    }
}
