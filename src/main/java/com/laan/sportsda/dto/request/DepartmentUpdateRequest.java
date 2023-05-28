package com.laan.sportsda.dto.request;

import com.laan.sportsda.util.MessagesUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentUpdateRequest {

    @NotBlank(message = MessagesUtil.AnnotationSupported.MANDATORY_DEPARTMENT_NAME)
    @Size(min = 2, max = 150, message = MessagesUtil.AnnotationSupported.INVALID_DEPARTMENT_NAME_SIZE)
    private String name;

    @NotBlank(message = MessagesUtil.AnnotationSupported.MANDATORY_FACULTY_ID)
    private String facultyId;

    @NotNull(message = MessagesUtil.AnnotationSupported.MANDATORY_VERSION)
    private Long version;
}
