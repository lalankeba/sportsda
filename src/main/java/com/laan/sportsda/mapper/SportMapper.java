package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.SportAddRequest;
import com.laan.sportsda.dto.request.SportUpdateRequest;
import com.laan.sportsda.dto.response.FeatureResponse;
import com.laan.sportsda.dto.response.PossibleValueResponse;
import com.laan.sportsda.dto.response.SportResponse;
import com.laan.sportsda.dto.response.SportShortResponse;
import com.laan.sportsda.entity.FeatureEntity;
import com.laan.sportsda.entity.PossibleValueEntity;
import com.laan.sportsda.entity.SportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SportMapper {

    @Mapping(target = "features", source = "featureEntities")
    SportResponse mapEntityToResponse(SportEntity entity);

    SportShortResponse mapEntityToShortResponse(SportEntity entity);

    List<SportShortResponse> mapEntitiesToShortResponses(List<SportEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", constant = "0L")
    @Mapping(target = "featureEntities", ignore = true)
    @Mapping(target = "memberEntities", ignore = true)
    SportEntity mapAddRequestToEntity(SportAddRequest addRequest);

    @Mapping(target = "id", source = "sportId")
    @Mapping(target = "featureEntities", ignore = true)
    @Mapping(target = "memberEntities", ignore = true)
    SportEntity mapUpdateRequestToEntity(SportUpdateRequest updateRequest, String sportId);

    List<FeatureResponse> mapEntitiesToFeatureResponses(List<FeatureEntity> featureEntities);

    @Mapping(target = "sportId", ignore = true)
    @Mapping(target = "possibleValues", source = "possibleValueEntities")
    FeatureResponse mapEntityToFeatureResponse(FeatureEntity featureEntity);

    List<PossibleValueResponse> mapEntitiesToPossibleValueResponses(List<PossibleValueEntity> possibleValueEntities);

    PossibleValueResponse mapEntityToPossibleValueResponse(PossibleValueEntity possibleValueEntity);
}
