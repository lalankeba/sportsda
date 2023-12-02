package com.laan.sportsda.service;

import com.laan.sportsda.dto.response.CountryResponse;

import java.util.List;

public interface CountryService {

    List<CountryResponse> getCountries();
}
