package com.laan.sportsda.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentUpdateRequest {

    @NotBlank(message = "{mandatory.department.name}")
    @Size(min = 2, max = 150, message = "{invalid.department.name.size}")
    private String name;

    @NotBlank(message = "{mandatory.faculty.id}")
    private String facultyId;

    @NotNull(message = "{mandatory.version}")
    private Long version;
}
