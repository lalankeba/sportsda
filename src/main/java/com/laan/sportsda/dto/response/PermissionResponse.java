package com.laan.sportsda.dto.response;

import com.laan.sportsda.enums.PermissionDescription;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionResponse {

    private String id;
    private PermissionDescription description;
}
