package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.response.PermissionResponse;
import com.laan.sportsda.entity.PermissionEntity;
import com.laan.sportsda.enums.PermissionDescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse mapEntityToResponse(PermissionEntity entity);

    List<PermissionResponse> mapEntitiesToResponses(List<PermissionEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "permissionDescription")
    @Mapping(target = "roleEntities", ignore = true)
    PermissionEntity mapEnumToEntity(PermissionDescription permissionDescription);

    List<PermissionEntity> mapStringsToPermissionEntities(List<String> values);

    @Mapping(target = "id", source = "value")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "roleEntities", ignore = true)
    PermissionEntity mapStringToPermissionEntity(String value);
}
