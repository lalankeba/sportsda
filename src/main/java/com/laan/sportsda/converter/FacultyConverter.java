package com.laan.sportsda.converter;

import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.entity.FacultyEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FacultyConverter {

    private final ModelMapper modelMapper;

    public FacultyConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public FacultyResponse convertEntityToResponse(final FacultyEntity facultyEntity) {
        return modelMapper.map(facultyEntity, FacultyResponse.class);
    }

    public List<FacultyResponse> convertEntitiesToResponses(final List<FacultyEntity> facultyEntities) {
        List<FacultyResponse> facultyResponses = new ArrayList<>();
        if (facultyEntities != null && !facultyEntities.isEmpty()) {
            facultyResponses = facultyEntities.stream().map(this::convertEntityToResponse).toList();
        }
        return facultyResponses;
    }

    public FacultyEntity convertAddRequestToEntity(final FacultyAddRequest facultyAddRequest) {
        return modelMapper.map(facultyAddRequest, FacultyEntity.class);
    }

    public FacultyEntity convertUpdateRequestToEntity(final FacultyUpdateRequest facultyUpdateRequest, final String id) {
        FacultyEntity facultyEntity = modelMapper.map(facultyUpdateRequest, FacultyEntity.class);
        facultyEntity.setId(id);
        return facultyEntity;
    }

}
