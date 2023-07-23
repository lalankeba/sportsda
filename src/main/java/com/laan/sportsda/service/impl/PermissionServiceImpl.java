package com.laan.sportsda.service.impl;

import com.laan.sportsda.dto.response.PermissionResponse;
import com.laan.sportsda.entity.PermissionEntity;
import com.laan.sportsda.mapper.PermissionMapper;
import com.laan.sportsda.repository.PermissionRepository;
import com.laan.sportsda.service.PermissionService;
import com.laan.sportsda.validator.PermissionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    private final PermissionValidator permissionValidator;

    private final PermissionMapper permissionMapper;

    @Override
    public PermissionResponse getPermission(String id) {
        Optional<PermissionEntity> optionalPermissionEntity = permissionRepository.findById(id);
        permissionValidator.validateNonExistingPermissionEntity(id, optionalPermissionEntity);

        PermissionResponse permissionResponse = null;
        if (optionalPermissionEntity.isPresent()) {
            permissionResponse = permissionMapper.mapEntityToResponse(optionalPermissionEntity.get());
        }
        return permissionResponse;
    }

    @Override
    public List<PermissionResponse> getPermissions() {
        List<PermissionEntity> permissionEntities = permissionRepository.findAll();
        return permissionMapper.mapEntitiesToResponses(permissionEntities);
    }

}
