package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.DepartmentAddRequest;
import com.laan.sportsda.dto.request.DepartmentUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.DepartmentShortResponse;
import com.laan.sportsda.entity.DepartmentEntity;
import com.laan.sportsda.entity.FacultyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "facultyId", source = "departmentEntity.facultyEntity.id")
    DepartmentResponse mapEntityToResponse(DepartmentEntity departmentEntity);

    List<DepartmentResponse> mapEntitiesToResponses(List<DepartmentEntity> departmentEntities);

    List<DepartmentShortResponse> mapEntitiesToShortResponses(List<DepartmentEntity> departmentEntities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "departmentAddRequest.name")
    @Mapping(target = "facultyEntity", source = "existingFacultyEntity")
    @Mapping(target = "memberEntities", ignore = true)
    @Mapping(target = "version", constant = "0L")
    DepartmentEntity mapAddRequestToEntity(DepartmentAddRequest departmentAddRequest, FacultyEntity existingFacultyEntity);

    @Mapping(target = "id", source = "departmentId")
    @Mapping(target = "name", source = "departmentUpdateRequest.name")
    @Mapping(target = "facultyEntity", source = "existingFacultyEntity")
    @Mapping(target = "memberEntities", ignore = true)
    @Mapping(target = "version", source = "departmentUpdateRequest.version")
    DepartmentEntity mapUpdateRequestToEntity(DepartmentUpdateRequest departmentUpdateRequest, String departmentId, FacultyEntity existingFacultyEntity);

}
