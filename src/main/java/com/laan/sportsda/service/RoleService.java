package com.laan.sportsda.service;

import com.laan.sportsda.dto.request.RoleAddRequest;
import com.laan.sportsda.dto.request.RoleUpdateRequest;
import com.laan.sportsda.dto.response.RoleResponse;
import com.laan.sportsda.dto.response.RolesResponse;

import java.util.List;

public interface RoleService {

    RoleResponse getRole(final String id);
    List<RolesResponse> getRoles();
    RoleResponse addRole(final RoleAddRequest roleAddRequest);
    RoleResponse updateRole(final String id, final RoleUpdateRequest roleUpdateRequest);
    void deleteRole(final String id);
}
