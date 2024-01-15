package com.laan.sportsda.dto.request;

import com.laan.sportsda.util.MessagesUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleUpdateRequest {

    @NotNull(message = MessagesUtil.AnnotationSupported.MANDATORY_ROLE_NAME)
    @Size(min = 2, max = 150, message = MessagesUtil.AnnotationSupported.INVALID_ROLE_NAME_SIZE)
    private String name;

    @NotNull(message = MessagesUtil.AnnotationSupported.MANDATORY_ROLE_DESCRIPTION)
    @Size(min = 2, max = 250, message = MessagesUtil.AnnotationSupported.INVALID_ROLE_DESCRIPTION_SIZE)
    private String description;

    private List<String> permissionIds;

}
