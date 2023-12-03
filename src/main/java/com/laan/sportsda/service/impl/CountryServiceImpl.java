package com.laan.sportsda.service.impl;

import com.laan.sportsda.client.CountryClient;
import com.laan.sportsda.client.response.CountryClientResponse;
import com.laan.sportsda.dto.response.CountryResponse;
import com.laan.sportsda.entity.CountryEntity;
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
            try {
                List<CountryClientResponse> countryClientResponses = countryClient.getAllCountries();
                log.info("{} countries received from external country service", countryClientResponses.size());
                countryResponses = countryMapper.mapClientResponsesToResponses(countryClientResponses);

                List<CountryEntity> newCountryEntities = countryMapper.mapClientResponsesToEntities(countryClientResponses, LocalDateTime.now());
                countryRepository.saveAll(newCountryEntities);
            } catch (Exception e) {
                log.error("Exception occurred when calling external country service", e);
                countryResponses = List.of();
            }
        } else { // has countries in db, but needs to check whether they are upto date
            log.info("Has country information in DB. Will request country details external service if they need to be updated.");
            countryResponses = countryMapper.mapEntitiesToResponses(countryEntities);

            LocalDateTime lastModifiedDateTime = countryEntities.get(0).getModifiedDateTime();
            LocalDateTime nextDateTime = lastModifiedDateTime.plusDays(propertyUtil.getCountryApiUpdateInterval());
            if (nextDateTime.isBefore(LocalDateTime.now())) {
                countryTask.getCountriesAndSave();
            } else {
                log.info("Didn't update the country information. Last updated date: {}, Next update date: {}", lastModifiedDateTime, nextDateTime);
            }

        }
        return countryResponses.stream().sorted(Comparator.comparing(CountryResponse::getCommonName)).toList();
    }
}
