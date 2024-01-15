package com.laan.sportsda.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public class RoleResponse {

    private String id;

    private String name;

    private String description;

    @ToString.Exclude
    private List<PermissionResponse> permissions;

}
