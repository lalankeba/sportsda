package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.FacultiesResponse;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.entity.DepartmentEntity;
import com.laan.sportsda.entity.FacultyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacultyMapper {

    @Mapping(target = "departments", source = "departmentEntities")
    FacultyResponse mapEntityToFacultyResponse(FacultyEntity entity);

    List<DepartmentResponse> mapDepartmentEntitiesToDepartmentResponses(List<DepartmentEntity> entities);

    @Mapping(target = "facultyId", ignore = true)
    DepartmentResponse mapDepartmentEntityToDepartmentResponse(DepartmentEntity entity);

    List<FacultiesResponse> mapEntitiesToFacultiesResponses(List<FacultyEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departmentEntities", ignore = true)
    @Mapping(target = "version", constant = "0L")
    FacultyEntity mapAddRequestToEntity(FacultyAddRequest addRequest);

    @Mapping(target = "id", source = "facultyId")
    @Mapping(target = "departmentEntities", ignore = true)
    FacultyEntity mapUpdateRequestToEntity(FacultyUpdateRequest updateRequest, String facultyId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departmentEntities", ignore = true)
    @Mapping(target = "version", constant = "0L")
    FacultyEntity mapDetailsToEntity(String name);
}
