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
    CountryResponse mapClientResponseToResponse(CountryClientResponse countryClientResponse);

    List<CountryResponse> mapClientResponsesToResponses(List<CountryClientResponse> countryClientResponses);

}
