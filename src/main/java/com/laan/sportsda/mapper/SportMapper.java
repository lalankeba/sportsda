package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.SportAddRequest;
import com.laan.sportsda.dto.request.SportUpdateRequest;
import com.laan.sportsda.dto.response.SportResponse;
import com.laan.sportsda.entity.SportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SportMapper {

    SportResponse mapEntityToResponse(SportEntity entity);

    List<SportResponse> mapEntitiesToResponses(List<SportEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", constant = "0L")
    SportEntity mapAddRequestToEntity(SportAddRequest addRequest);

    @Mapping(target = "id", source = "sportId")
    SportEntity mapUpdateRequestToEntity(SportUpdateRequest updateRequest, String sportId);
}
