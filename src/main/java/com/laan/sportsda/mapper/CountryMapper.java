package com.laan.sportsda.mapper;

import com.laan.sportsda.client.response.CountryClientResponse;
import com.laan.sportsda.dto.response.CountryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    @Mapping(target = "commonName", source = "name.common")
    @Mapping(target = "officialName", source = "name.common")
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
}
