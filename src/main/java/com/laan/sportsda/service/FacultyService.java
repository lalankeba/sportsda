package com.laan.sportsda.service;

import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.FacultiesResponse;
import com.laan.sportsda.dto.response.FacultyResponse;

import java.util.List;

public interface FacultyService {

    FacultyResponse getFaculty(final String id);
    List<FacultiesResponse> getFaculties();
    FacultyResponse addFaculty(final FacultyAddRequest facultyAddRequest);
    FacultyResponse updateFaculty(final String id, final FacultyUpdateRequest facultyUpdateRequest);
    void deleteFaculty(final String id);
}
