package com.laan.sportsda.service;

import com.laan.sportsda.dto.request.SportAddRequest;
import com.laan.sportsda.dto.request.SportUpdateRequest;
import com.laan.sportsda.dto.response.SportResponse;
import com.laan.sportsda.dto.response.SportShortResponse;

import java.util.List;

public interface SportService {

    SportResponse getSport(final String id);
    List<SportShortResponse> getSports();
    SportResponse addSport(final SportAddRequest sportAddRequest);
    SportResponse updateSport(final String id, final SportUpdateRequest sportUpdateRequest);
    void deleteSport(final String id);
}
