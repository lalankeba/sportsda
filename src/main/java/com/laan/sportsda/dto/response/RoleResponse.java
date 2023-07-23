package com.laan.sportsda.dto.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class RoleResponse {

    private String id;

    private String name;

    private String description;

    @ToString.Exclude
    private List<PermissionResponse> permissions;

    private Long version;
}
