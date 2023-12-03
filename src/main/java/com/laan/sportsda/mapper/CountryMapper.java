package com.laan.sportsda.mapper;

import com.laan.sportsda.client.response.CountryClientResponse;
import com.laan.sportsda.dto.response.CountryResponse;
import com.laan.sportsda.entity.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    @Mapping(target = "commonName", source = "name.common")
    @Mapping(target = "officialName", source = "name.official")
    @Mapping(target = "capitals", source = "capital")
    @Mapping(target = "flagPng", source = "flags.png")
    @Mapping(target = "flagSvg", source = "flags.svg")
    CountryResponse mapClientResponseToResponse(CountryClientResponse countryClientResponse);

    List<CountryResponse> mapClientResponsesToResponses(List<CountryClientResponse> countryClientResponses);

    default String mapListToString(List<String> values) {
        if (values == null) {
            return null;
        }
        return String.join(", ", values);
    }

    @Mapping(target = "commonName", source = "countryClientResponse.name.common")
    @Mapping(target = "officialName", source = "countryClientResponse.name.official")
    @Mapping(target = "capitals", source = "countryClientResponse.capital")
    @Mapping(target = "flagPng", source = "countryClientResponse.flags.png")
    @Mapping(target = "flagSvg", source = "countryClientResponse.flags.svg")
    @Mapping(target = "modifiedDateTime", source = "modifyDateTime")
    CountryEntity mapClientResponseToEntity(CountryClientResponse countryClientResponse, LocalDateTime modifyDateTime);

    default List<CountryEntity> mapClientResponsesToEntities(List<CountryClientResponse> countryClientResponses, LocalDateTime modifyDateTime) {
        if (countryClientResponses == null) {
            return Collections.emptyList();
        }
        return countryClientResponses.stream()
                .filter(countryClientResponse -> countryClientResponse.getCca3() != null)
                .map(countryClientResponse -> mapClientResponseToEntity(countryClientResponse, modifyDateTime))
                .toList();
    }

    List<CountryResponse> mapEntitiesToResponses(List<CountryEntity> countryEntities);
}
