package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.DepartmentAddRequest;
import com.laan.sportsda.dto.request.DepartmentUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.DepartmentShortResponse;
import com.laan.sportsda.entity.DepartmentEntity;
import com.laan.sportsda.entity.FacultyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    DepartmentEntity mapAddRequestToEntity(DepartmentAddRequest departmentAddRequest, FacultyEntity existingFacultyEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "facultyEntity", ignore = true)
    @Mapping(target = "memberEntities", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    DepartmentEntity updateEntityFromUpdateRequest(DepartmentUpdateRequest departmentUpdateRequest, @MappingTarget DepartmentEntity departmentEntity);

}
