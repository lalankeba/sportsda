package com.laan.sportsda.client;

import com.laan.sportsda.client.response.CountryClientResponse;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface CountryClient {

    @GetExchange("/all")
    List<CountryClientResponse> getAllCountries();
}
