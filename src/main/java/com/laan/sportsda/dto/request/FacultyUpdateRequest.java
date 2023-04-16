package com.laan.sportsda.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FacultyUpdateRequest {

    @NotNull(message = "Name is mandatory")
    @Size(min = 2, max = 150, message = "Name must be valid value between 2 and 150 characters")
    private String name;

    @NotNull(message = "Version cannot be empty")
    private Long version;
}
