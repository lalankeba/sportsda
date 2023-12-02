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
        log.info("Requesting country information from external service");
        List<CountryClientResponse> countryClientResponses = countryClient.getAllCountries();
        log.info("Response received from external service");
        return countryMapper.mapClientResponsesToResponses(countryClientResponses);
    }
}
