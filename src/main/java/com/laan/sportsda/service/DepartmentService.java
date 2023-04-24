package com.laan.sportsda.service;

import com.laan.sportsda.dto.request.DepartmentAddRequest;
import com.laan.sportsda.dto.request.DepartmentUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse getDepartment(final String id);
    List<DepartmentResponse> getDepartments();
    DepartmentResponse addDepartment(final DepartmentAddRequest departmentAddRequest);
    DepartmentResponse updateDepartment(final String id, final DepartmentUpdateRequest departmentUpdateRequest);
    void deleteDepartment(final String id);
    List<DepartmentResponse> getDepartmentsByFaculty(final String facultyId);
}
