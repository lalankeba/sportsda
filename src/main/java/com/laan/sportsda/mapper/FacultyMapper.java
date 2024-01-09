package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.dto.response.FacultyShortResponse;
import com.laan.sportsda.entity.DepartmentEntity;
import com.laan.sportsda.entity.FacultyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacultyMapper {

    @Mapping(target = "departments", source = "departmentEntities")
    FacultyResponse mapEntityToFacultyResponse(FacultyEntity entity);

    FacultyShortResponse mapEntityToFacultyShortResponse(FacultyEntity entity);

    List<DepartmentResponse> mapDepartmentEntitiesToDepartmentResponses(List<DepartmentEntity> entities);

    @Mapping(target = "facultyId", ignore = true)
    DepartmentResponse mapDepartmentEntityToDepartmentResponse(DepartmentEntity entity);

    List<FacultyShortResponse> mapEntitiesToFacultyShortResponses(List<FacultyEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departmentEntities", ignore = true)
    @Mapping(target = "version", constant = "0L")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    FacultyEntity mapAddRequestToEntity(FacultyAddRequest addRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departmentEntities", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    FacultyEntity updateEntityFromUpdateRequest(FacultyUpdateRequest updateRequest, @MappingTarget FacultyEntity facultyEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departmentEntities", ignore = true)
    @Mapping(target = "version", constant = "0L")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    FacultyEntity mapDetailsToEntity(String name);
}
