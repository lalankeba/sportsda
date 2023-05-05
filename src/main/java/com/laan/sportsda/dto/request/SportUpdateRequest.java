package com.laan.sportsda.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SportUpdateRequest {

    @NotNull(message = "{mandatory.sport.name}")
    @Size(min = 2, max = 150, message = "{invalid.sport.name.size}")
    private String name;

    @NotNull(message = "{mandatory.version}")
    private Long version;
}
