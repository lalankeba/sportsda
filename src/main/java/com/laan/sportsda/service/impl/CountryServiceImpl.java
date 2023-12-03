package com.laan.sportsda.service.impl;

import com.laan.sportsda.client.CountryClient;
import com.laan.sportsda.client.response.CountryClientResponse;
import com.laan.sportsda.dto.response.CountryResponse;
import com.laan.sportsda.mapper.CountryMapper;
import com.laan.sportsda.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryServiceImpl implements CountryService {

    private final CountryClient countryClient;

    private final CountryMapper countryMapper;

    @Override
    public List<CountryResponse> getCountries() {
        List<CountryResponse> countryResponses;
        log.info("Requesting country information from external service");
        try {
            List<CountryClientResponse> countryClientResponses = countryClient.getAllCountries();
            log.info("{} Countries received from external country service", countryClientResponses.size());
            countryResponses = countryMapper.mapClientResponsesToResponses(countryClientResponses);
        } catch (Exception e) {
            log.error("Exception occurred when calling external country service", e);
            countryResponses = List.of();
        }
        return countryResponses;
    }
}
