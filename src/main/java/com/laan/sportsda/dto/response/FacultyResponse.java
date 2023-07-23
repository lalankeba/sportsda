package com.laan.sportsda.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class FacultyResponse {

    private String id;

    private String name;

    private List<DepartmentResponse> departments;

    private Long version;
}
