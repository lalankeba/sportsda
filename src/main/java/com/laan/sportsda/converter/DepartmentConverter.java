package com.laan.sportsda.converter;

import com.laan.sportsda.dto.request.DepartmentAddRequest;
import com.laan.sportsda.dto.request.DepartmentUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.entity.DepartmentEntity;
import com.laan.sportsda.entity.FacultyEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentConverter {

    private final ModelMapper modelMapper;

    public DepartmentConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DepartmentResponse convertEntityToResponse(final DepartmentEntity departmentEntity) {
        DepartmentResponse departmentResponse = modelMapper.map(departmentEntity, DepartmentResponse.class);
        departmentResponse.setFacultyId(departmentEntity.getFacultyEntity().getId());
        return departmentResponse;
    }

    public List<DepartmentResponse> convertEntitiesToResponses(final List<DepartmentEntity> departmentEntities) {
        List<DepartmentResponse> departmentResponses = new ArrayList<>();
        if (departmentEntities != null && !departmentEntities.isEmpty()) {
            departmentResponses = departmentEntities.stream().map(this::convertEntityToResponse).toList();
        }
        return departmentResponses;
    }

    public DepartmentEntity convertAddRequestToEntity(final DepartmentAddRequest departmentAddRequest, final FacultyEntity facultyEntity) {
        DepartmentEntity departmentEntity = modelMapper.map(departmentAddRequest, DepartmentEntity.class);
        departmentEntity.setFacultyEntity(facultyEntity);
        return departmentEntity;
    }

    public DepartmentEntity convertUpdateRequestToEntity(final DepartmentUpdateRequest departmentUpdateRequest, final String id, final FacultyEntity facultyEntity) {
        DepartmentEntity departmentEntity = modelMapper.map(departmentUpdateRequest, DepartmentEntity.class);
        departmentEntity.setId(id);
        departmentEntity.setFacultyEntity(facultyEntity);
        return departmentEntity;
    }
}
