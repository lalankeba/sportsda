package com.laan.sportsda.service.impl;

import com.laan.sportsda.dto.request.RoleAddRequest;
import com.laan.sportsda.dto.request.RoleUpdateRequest;
import com.laan.sportsda.dto.response.RoleResponse;
import com.laan.sportsda.dto.response.RoleShortResponse;
import com.laan.sportsda.entity.RoleEntity;
import com.laan.sportsda.mapper.RoleMapper;
import com.laan.sportsda.repository.RoleRepository;
import com.laan.sportsda.service.RoleService;
import com.laan.sportsda.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleValidator roleValidator;

    private final RoleMapper roleMapper;

    @Override
    public RoleResponse getRole(final String id) {
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(id);
        roleValidator.validateNonExistingRoleEntity(id, optionalRoleEntity);

        RoleResponse roleResponse = null;
        if (optionalRoleEntity.isPresent()) {
            roleResponse = roleMapper.mapEntityToRoleResponse(optionalRoleEntity.get());
        }
        return roleResponse;
    }

    @Override
    public List<RoleShortResponse> getRoles() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        return roleMapper.mapEntitiesToShortResponses(roleEntities);
    }

    @Override
    @Transactional
    public RoleResponse addRole(final RoleAddRequest roleAddRequest) {
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findByName(roleAddRequest.getName());
        roleValidator.validateDuplicateRoleEntity(optionalRoleEntity);

        RoleResponse roleResponse = null;
        if (optionalRoleEntity.isEmpty()) {
            RoleEntity roleEntity = roleMapper.mapAddRequestToEntity(roleAddRequest);
            RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
            roleResponse = roleMapper.mapEntityToRoleResponse(savedRoleEntity);
        }
        return roleResponse;
    }

    @Override
    @Transactional
    public RoleResponse updateRole(final String id, final RoleUpdateRequest roleUpdateRequest) {
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(id);
        roleValidator.validateNonExistingRoleEntity(id, optionalRoleEntity);

        Optional<RoleEntity> optionalRoleEntityByName = roleRepository.findByNameAndIdNotContains(roleUpdateRequest.getName(), id);
        roleValidator.validateDuplicateRoleEntity(optionalRoleEntityByName);

        RoleEntity roleEntity = roleMapper.mapUpdateRequestToEntity(roleUpdateRequest, id);
        RoleEntity updatedRoleEntity = roleRepository.saveAndFlush(roleEntity);
        return roleMapper.mapEntityToRoleResponse(updatedRoleEntity);
    }

    @Override
    @Transactional
    public void deleteRole(final String id) {
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(id);
        roleValidator.validateNonExistingRoleEntity(id, optionalRoleEntity);
        roleRepository.deleteById(id);
    }
}
