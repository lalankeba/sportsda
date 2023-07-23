package com.laan.sportsda.dto.response;

import com.laan.sportsda.enums.PermissionDescription;
import lombok.Data;

@Data
public class PermissionResponse {

    private String id;
    private PermissionDescription description;
}
