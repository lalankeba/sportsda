package com.laan.sportsda.service;

import com.laan.sportsda.client.CountryClient;
import com.laan.sportsda.client.response.CountryClientResponse;
import com.laan.sportsda.client.response.CountryFlag;
import com.laan.sportsda.client.response.CountryName;
import com.laan.sportsda.dto.response.CountryResponse;
import com.laan.sportsda.entity.CountryEntity;
import com.laan.sportsda.exception.ElementNotFoundException;
import com.laan.sportsda.mapper.CountryMapper;
import com.laan.sportsda.repository.CountryRepository;
import com.laan.sportsda.service.impl.CountryServiceImpl;
import com.laan.sportsda.task.CountryTask;
import com.laan.sportsda.util.PropertyUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

class CountryServiceTest {

    private CountryService countryService;

    @Mock
    private CountryClient countryClient;

    @Mock
    private CountryMapper countryMapper;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryTask countryTask;

    @Mock
    private PropertyUtil propertyUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        countryService = new CountryServiceImpl(countryClient, countryMapper, countryRepository, countryTask, propertyUtil);
    }

    @Test
    void shouldReturnCountriesFromExternalService() {
        List<CountryClientResponse> countryClientResponses = List.of(createDummyAlbaniaCountryClientResponse(),
                createDummyJapanCountryClientResponse(), createDummyUSCountryClientResponse());
        List<CountryResponse> countryResponses = List.of(createDummyAlbaniaCountryResponse(),
                createDummyJapanCountryResponse(), createDummyUSCountryResponse());

        given(countryRepository.findAll()).willReturn(List.of());
        given(countryClient.getAllCountries()).willReturn(countryClientResponses);
        doNothing().when(countryTask).saveCountries(countryClientResponses);
        given(countryMapper.mapClientResponsesToResponses(countryClientResponses)).willReturn(countryResponses);

        List<CountryResponse> result = countryService.getCountries();

        assertThat(result, is(countryResponses));
    }

    @Test
    void shouldReturnCountriesFromDatabaseWithUpdate() {
        List<CountryEntity> countryEntities = List.of(createDummyAlbaniaCountryEntity(LocalDateTime.now().minusDays(50)),
                createDummyUSCountryEntity(LocalDateTime.now().minusDays(50)), createDummyJapanCountryEntity(LocalDateTime.now().minusDays(50)));
        List<CountryClientResponse> countryClientResponses = List.of(createDummyAlbaniaCountryClientResponse(),
                createDummyJapanCountryClientResponse(), createDummyUSCountryClientResponse());
        List<CountryResponse> countryResponses = List.of(createDummyAlbaniaCountryResponse(),
                createDummyJapanCountryResponse(), createDummyUSCountryResponse());

        given(countryRepository.findAll()).willReturn(countryEntities);
        given(propertyUtil.getCountryApiUpdateInterval()).willReturn(7L);
        given(countryClient.getAllCountries()).willReturn(countryClientResponses);
        doNothing().when(countryTask).saveCountries(countryClientResponses);
        given(countryMapper.mapClientResponsesToResponses(countryClientResponses)).willReturn(countryResponses);

        List<CountryResponse> result = countryService.getCountries();

        assertThat(result, is(countryResponses));
    }

    @Test
    void shouldReturnCountriesFromDatabaseWithoutUpdate() {
        List<CountryEntity> countryEntities = List.of(createDummyAlbaniaCountryEntity(LocalDateTime.now()),
                createDummyUSCountryEntity(LocalDateTime.now()), createDummyJapanCountryEntity(LocalDateTime.now()));
        List<CountryResponse> countryResponses = List.of(createDummyAlbaniaCountryResponse(),
                createDummyJapanCountryResponse(), createDummyUSCountryResponse());

        given(countryRepository.findAll()).willReturn(countryEntities);
        given(propertyUtil.getCountryApiUpdateInterval()).willReturn(7L);
        given(countryMapper.mapEntitiesToResponses(countryEntities)).willReturn(countryResponses);

        List<CountryResponse> result = countryService.getCountries();

        assertThat(result, is(countryResponses));
    }

    @Test
    void shouldThrowExceptionFromExternalService() {
        given(countryRepository.findAll()).willReturn(List.of());
        doThrow(new ClassCastException("Class cannot be converted to designated type"))
                .when(countryClient).getAllCountries();

        Assertions.assertThrows(ElementNotFoundException.class, () -> countryService.getCountries());
    }

    private CountryEntity createDummyAlbaniaCountryEntity(LocalDateTime modifiedDateTime) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setCca2("AL");
        countryEntity.setCca3("ALB");
        countryEntity.setCcn3("008");
        countryEntity.setCommonName("Albania");
        countryEntity.setOfficialName("Republic of Albania");
        countryEntity.setCapitals("Tirana");
        countryEntity.setArea(28748.0);
        countryEntity.setPopulation(2837743L);
        countryEntity.setRegion("Europe");
        countryEntity.setSubregion("Southeast Europe");
        countryEntity.setContinents("Europe");
        countryEntity.setFlagPng("https://flagcdn.com/w320/al.png");
        countryEntity.setFlagSvg("https://flagcdn.com/al.svg");
        countryEntity.setModifiedDateTime(modifiedDateTime);
        return countryEntity;
    }

    private CountryEntity createDummyJapanCountryEntity(LocalDateTime modifiedDateTime) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setCca2("JP");
        countryEntity.setCca3("JPN");
        countryEntity.setCcn3("392");
        countryEntity.setCommonName("Japan");
        countryEntity.setOfficialName("Japan");
        countryEntity.setCapitals("Tokyo");
        countryEntity.setArea(377930.0);
        countryEntity.setPopulation(125836021L);
        countryEntity.setRegion("Asia");
        countryEntity.setSubregion("Eastern Asia");
        countryEntity.setContinents("Asia");
        countryEntity.setFlagPng("https://flagcdn.com/w320/jp.png");
        countryEntity.setFlagSvg("https://flagcdn.com/jp.svg");
        countryEntity.setModifiedDateTime(modifiedDateTime);
        return countryEntity;
    }

    private CountryEntity createDummyUSCountryEntity(LocalDateTime modifiedDateTime) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setCca2("US");
        countryEntity.setCca3("USA");
        countryEntity.setCcn3("840");
        countryEntity.setCommonName("United States");
        countryEntity.setOfficialName("United States of America");
        countryEntity.setCapitals("Washington, D.C.");
        countryEntity.setArea(9372610.0);
        countryEntity.setPopulation(329484123L);
        countryEntity.setRegion("Americas");
        countryEntity.setSubregion("North America");
        countryEntity.setContinents("North America");
        countryEntity.setFlagPng("https://flagcdn.com/w320/us.png");
        countryEntity.setFlagSvg("https://flagcdn.com/us.svg");
        countryEntity.setModifiedDateTime(modifiedDateTime);
        return countryEntity;
    }

    private CountryClientResponse createDummyAlbaniaCountryClientResponse() {
        CountryEntity countryEntity = createDummyAlbaniaCountryEntity(LocalDateTime.now());
        return createDummyCountryClientResponseFromEntity(countryEntity);
    }

    private CountryClientResponse createDummyJapanCountryClientResponse() {
        CountryEntity countryEntity = createDummyJapanCountryEntity(LocalDateTime.now());
        return createDummyCountryClientResponseFromEntity(countryEntity);
    }

    private CountryClientResponse createDummyUSCountryClientResponse() {
        CountryEntity countryEntity = createDummyUSCountryEntity(LocalDateTime.now());
        return createDummyCountryClientResponseFromEntity(countryEntity);
    }

    private CountryClientResponse createDummyCountryClientResponseFromEntity(CountryEntity countryEntity) {
        CountryClientResponse clientResponse = new CountryClientResponse();
        CountryName countryName = new CountryName();
        countryName.setCommon(countryEntity.getCommonName());
        countryName.setOfficial(countryEntity.getOfficialName());
        clientResponse.setName(countryName);
        clientResponse.setCca2(countryEntity.getCca2());
        clientResponse.setCca3(countryEntity.getCca3());
        clientResponse.setCcn3(countryEntity.getCcn3());
        clientResponse.setCapital(Arrays.stream(countryEntity.getCapitals().split(", ")).toList());
        clientResponse.setArea(countryEntity.getArea());
        clientResponse.setPopulation(countryEntity.getPopulation());
        clientResponse.setRegion(countryEntity.getRegion());
        clientResponse.setSubregion(countryEntity.getSubregion());
        clientResponse.setContinents(Arrays.stream(countryEntity.getContinents().split(", ")).toList());
        CountryFlag countryFlag = new CountryFlag();
        countryFlag.setPng(countryEntity.getFlagPng());
        countryFlag.setSvg(countryEntity.getFlagSvg());
        clientResponse.setFlags(countryFlag);
        return clientResponse;
    }

    private CountryResponse createDummyAlbaniaCountryResponse() {
        CountryEntity countryEntity = createDummyAlbaniaCountryEntity(LocalDateTime.now());
        return createDummyCountryResponseFromEntity(countryEntity);
    }

    private CountryResponse createDummyJapanCountryResponse() {
        CountryEntity countryEntity = createDummyJapanCountryEntity(LocalDateTime.now());
        return createDummyCountryResponseFromEntity(countryEntity);
    }

    private CountryResponse createDummyUSCountryResponse() {
        CountryEntity countryEntity = createDummyUSCountryEntity(LocalDateTime.now());
        return createDummyCountryResponseFromEntity(countryEntity);
    }

    private CountryResponse createDummyCountryResponseFromEntity(CountryEntity countryEntity) {
        CountryResponse countryResponse = new CountryResponse();
        countryResponse.setCommonName(countryEntity.getCommonName());
        countryResponse.setOfficialName(countryEntity.getOfficialName());
        countryResponse.setCca2(countryEntity.getCca2());
        countryResponse.setCca3(countryEntity.getCca3());
        countryResponse.setCcn3(countryEntity.getCcn3());
        countryResponse.setCapitals(countryEntity.getCapitals());
        countryResponse.setArea(countryEntity.getArea());
        countryResponse.setPopulation(countryEntity.getPopulation());
        countryResponse.setRegion(countryEntity.getRegion());
        countryResponse.setSubregion(countryEntity.getSubregion());
        countryResponse.setContinents(countryEntity.getContinents());
        countryResponse.setFlagPng(countryEntity.getFlagPng());
        countryResponse.setFlagSvg(countryEntity.getFlagSvg());
        return countryResponse;
    }

}
