package com.laan.sportsda.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentAddRequest {

    @NotBlank(message = "{mandatory.faculty.name}")
    @Size(min = 2, max = 150, message = "{invalid.department.name.size}")
    private String name;

    @NotBlank(message = "{mandatory.faculty.id}")
    private String facultyId;

}
