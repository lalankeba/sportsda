package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.RoleAddRequest;
import com.laan.sportsda.dto.request.RoleUpdateRequest;
import com.laan.sportsda.dto.response.RoleResponse;
import com.laan.sportsda.dto.response.RoleShortResponse;
import com.laan.sportsda.entity.PermissionEntity;
import com.laan.sportsda.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {

    @Mapping(target = "permissions", source = "permissionEntities")
    RoleResponse mapEntityToRoleResponse(RoleEntity entity);

    RoleShortResponse mapEntityToRoleShortResponse(RoleEntity entity);

    List<RoleShortResponse> mapEntitiesToShortResponses(List<RoleEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permissionEntities", source = "permissionIds")
    RoleEntity mapAddRequestToEntity(RoleAddRequest addRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permissionEntities", source = "updateRequest.permissionIds")
    void updateEntityFromUpdateRequest(RoleUpdateRequest updateRequest, @MappingTarget RoleEntity roleEntity);

    @Mapping(target = "id", ignore = true)
    RoleEntity mapDetailsToEntity(String name, String description, List<PermissionEntity> permissionEntities);

}
