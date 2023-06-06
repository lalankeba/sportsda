package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.entity.FacultyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacultyMapper {

    FacultyResponse mapEntityToResponse(FacultyEntity entity);

    List<FacultyResponse> mapEntitiesToResponses(List<FacultyEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", constant = "0L")
    FacultyEntity mapAddRequestToEntity(FacultyAddRequest addRequest);

    @Mapping(target = "id", source = "facultyId")
    FacultyEntity mapUpdateRequestToEntity(FacultyUpdateRequest updateRequest, String facultyId);
}
