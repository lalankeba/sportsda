package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.RoleAddRequest;
import com.laan.sportsda.dto.request.RoleUpdateRequest;
import com.laan.sportsda.dto.response.PermissionResponse;
import com.laan.sportsda.dto.response.RoleResponse;
import com.laan.sportsda.dto.response.RoleShortResponse;
import com.laan.sportsda.entity.PermissionEntity;
import com.laan.sportsda.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", source = "permissionEntities")
    RoleResponse mapEntityToRoleResponse(RoleEntity entity);

    List<PermissionResponse> mapPermissionEntitiesToPermissionResponses(List<PermissionEntity> entities);

    PermissionResponse mapPermissionEntityToPermissionResponse(PermissionEntity entity);

    RoleShortResponse mapEntityToRoleShortResponse(RoleEntity entity);

    List<RoleShortResponse> mapEntitiesToShortResponses(List<RoleEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permissionEntities", source = "permissionIds")
    @Mapping(target = "version", constant = "0L")
    RoleEntity mapAddRequestToEntity(RoleAddRequest addRequest);

    List<PermissionEntity> mapStringsToPermissionEntities(List<String> values);

    @Mapping(target = "id", source = "value")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "roleEntities", ignore = true)
    PermissionEntity mapStringToPermissionEntity(String value);

    @Mapping(target = "id", source = "roleId")
    @Mapping(target = "permissionEntities", source = "updateRequest.permissionIds")
    RoleEntity mapUpdateRequestToEntity(RoleUpdateRequest updateRequest, String roleId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", constant = "0L")
    RoleEntity mapDetailsToEntity(String name, String description, List<PermissionEntity> permissionEntities);

}
