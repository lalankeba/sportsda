package com.laan.sportsda.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FacultyResponse {

    private String id;

    private String name;

    private List<DepartmentResponse> departments;

    private Long version;
}
