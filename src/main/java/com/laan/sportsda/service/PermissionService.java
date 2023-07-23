package com.laan.sportsda.service;

import com.laan.sportsda.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    PermissionResponse getPermission(final String id);
    List<PermissionResponse> getPermissions();
}
