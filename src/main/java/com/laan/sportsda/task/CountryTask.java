package com.laan.sportsda.task;

import com.laan.sportsda.client.CountryClient;
import com.laan.sportsda.client.response.CountryClientResponse;
import com.laan.sportsda.entity.CountryEntity;
import com.laan.sportsda.mapper.CountryMapper;
import com.laan.sportsda.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CountryTask {

    private final CountryClient countryClient;

    private final CountryMapper countryMapper;

    private final CountryRepository countryRepository;

    @Async
    public void getCountriesAndSave() {
        try {
            List<CountryClientResponse> countryClientResponses = countryClient.getAllCountries();
            log.info("{} Countries received from external country service", countryClientResponses.size());
            List<CountryEntity> countryEntities = countryMapper.mapClientResponsesToEntities(countryClientResponses, LocalDateTime.now());

            countryRepository.saveAll(countryEntities);
            log.info("Saved {} country information in the DB", countryEntities.size());
        } catch (Exception e) {
            log.error("Exception occurred when get countries and save them in DB", e);
        }
    }
}
