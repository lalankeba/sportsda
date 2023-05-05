package com.laan.sportsda.dto.request;

import com.laan.sportsda.util.MessagesUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FacultyAddRequest {

    @NotNull(message = "{mandatory.faculty.name}")
    @Size(min = 2, max = 150, message = "{" + MessagesUtil.INVALID_FACULTY_NAME_SIZE + "}")
    private String name;

}
