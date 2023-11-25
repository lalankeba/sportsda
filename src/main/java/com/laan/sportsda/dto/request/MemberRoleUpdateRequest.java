package com.laan.sportsda.dto.request;

import com.laan.sportsda.util.MessagesUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRoleUpdateRequest {

    @NotBlank(message = MessagesUtil.AnnotationSupported.MANDATORY_ROLE_ID)
    private String roleId;
}
