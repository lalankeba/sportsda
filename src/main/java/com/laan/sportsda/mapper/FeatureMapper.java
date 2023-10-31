package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.FeatureAddRequest;
import com.laan.sportsda.dto.request.FeatureUpdateRequest;
import com.laan.sportsda.dto.response.FeatureResponse;
import com.laan.sportsda.dto.response.PossibleValueResponse;
import com.laan.sportsda.entity.FeatureEntity;
import com.laan.sportsda.entity.PossibleValueEntity;
import com.laan.sportsda.entity.SportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeatureMapper {

    @Mapping(target = "sportId", source = "featureEntity.sportEntity.id")
    @Mapping(target = "possibleValues", source = "featureEntity.possibleValueEntities")
    FeatureResponse mapEntityToResponse(FeatureEntity featureEntity);

    List<FeatureResponse> mapEntitiesToResponses(List<FeatureEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "featureAddRequest.name")
    @Mapping(target = "sportEntity", source = "existingSportEntity")
    @Mapping(target = "possibleValueEntities", ignore = true)
    @Mapping(target = "version", constant = "0L")
    FeatureEntity mapAddRequestToEntity(FeatureAddRequest featureAddRequest, SportEntity existingSportEntity);

    List<PossibleValueResponse> mapEntitiesToPossibleValueResponses(List<PossibleValueEntity> entities);

    PossibleValueResponse mapEntityToPossibleValueResponse(PossibleValueEntity possibleValueEntity);

    @Mapping(target = "id", source = "featureId")
    @Mapping(target = "name", source = "featureUpdateRequest.name")
    @Mapping(target = "sportEntity", source = "existingSportEntity")
    @Mapping(target = "possibleValueEntities", ignore = true)
    @Mapping(target = "version", source = "featureUpdateRequest.version")
    FeatureEntity mapUpdateRequestToEntity(FeatureUpdateRequest featureUpdateRequest, String featureId, SportEntity existingSportEntity);
}
