package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.FeatureAddRequest;
import com.laan.sportsda.dto.response.FeatureResponse;
import com.laan.sportsda.entity.FeatureEntity;
import com.laan.sportsda.entity.SportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeatureMapper {

    @Mapping(target = "sportId", source = "featureEntity.sportEntity.id")
    FeatureResponse mapEntityToResponse(FeatureEntity featureEntity);

    List<FeatureResponse> mapEntitiesToResponses(List<FeatureEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "featureAddRequest.name")
    @Mapping(target = "sportEntity", source = "existingSportEntity")
    @Mapping(target = "version", constant = "0L")
    FeatureEntity mapAddRequestToEntity(FeatureAddRequest featureAddRequest, SportEntity existingSportEntity);

}
