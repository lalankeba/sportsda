package com.laan.sportsda.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FacultyUpdateRequest {

    @NotNull(message = "{mandatory.faculty.name}")
    @Size(min = 2, max = 150, message = "{invalid.faculty.name.size}")
    private String name;

    @NotNull(message = "{mandatory.version}")
    private Long version;
}
