package com.laan.sportsda.service.impl;

import com.laan.sportsda.client.CountryClient;
import com.laan.sportsda.client.response.CountryClientResponse;
import com.laan.sportsda.dto.response.CountryResponse;
import com.laan.sportsda.entity.CountryEntity;
import com.laan.sportsda.exception.ElementNotFoundException;
import com.laan.sportsda.mapper.CountryMapper;
import com.laan.sportsda.repository.CountryRepository;
import com.laan.sportsda.service.CountryService;
import com.laan.sportsda.task.CountryTask;
import com.laan.sportsda.util.PropertyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryServiceImpl implements CountryService {

    private final CountryClient countryClient;

    private final CountryMapper countryMapper;

    private final CountryRepository countryRepository;

    private final CountryTask countryTask;

    private final PropertyUtil propertyUtil;

    @Override
    public List<CountryResponse> getCountries() {
        List<CountryResponse> countryResponses;
        List<CountryEntity> countryEntities = countryRepository.findAll();
        if (countryEntities.isEmpty()) { // no countries in db, needs to call external service
            log.info("No country information in DB. Requesting country details from external service");
            countryResponses = getCountriesAndSave();
        } else { // has countries in db, but needs to check whether they are upto date
            LocalDateTime lastModifiedDateTime = countryEntities.getFirst().getModifiedDateTime();
            if (needsToRenewCountries(lastModifiedDateTime)) {
                log.info("Has country information in DB, but needs to be updated.");
                countryResponses = getCountriesAndSave();
            } else {
                countryResponses = countryMapper.mapEntitiesToResponses(countryEntities);
            }
        }
        return countryResponses.stream().sorted(Comparator.comparing(CountryResponse::getCommonName)).toList();
    }

    private List<CountryResponse> getCountriesAndSave() {
        List<CountryClientResponse> countryClientResponses = getCountriesFromExternalService();
        countryTask.saveCountries(countryClientResponses);
        return countryMapper.mapClientResponsesToResponses(countryClientResponses);
    }

    private List<CountryClientResponse> getCountriesFromExternalService() {
        try {
            return countryClient.getAllCountries();
        } catch (Exception e) {
            throw new ElementNotFoundException("Cannot download countries. " + e.getMessage());
        }
    }

    private boolean needsToRenewCountries(final LocalDateTime lastModifiedDateTime) {
        LocalDateTime nextDateTime = lastModifiedDateTime.plusDays(propertyUtil.getCountryApiUpdateInterval());
        log.info("Country update information. Last updated date: {}, Next update date: {}", lastModifiedDateTime, nextDateTime);
        return nextDateTime.isBefore(LocalDateTime.now());
    }
}
