package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.response.PossibleValueResponse;
import com.laan.sportsda.entity.FeatureEntity;
import com.laan.sportsda.entity.PossibleValueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PossibleValueMapper {

    PossibleValueResponse mapEntityToResponse(PossibleValueEntity possibleValueEntity);

    List<PossibleValueResponse> mapEntitiesToResponses(List<PossibleValueEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attributeValue", source = "value")
    @Mapping(target = "featureEntity", ignore = true)
    PossibleValueEntity mapStringToEntity(String value);

    List<PossibleValueEntity> mapStringsToEntities(List<String> values);

    default List<PossibleValueEntity> mapStringsAndEntityToEntities(List<String> values, FeatureEntity featureEntity) {
        List<PossibleValueEntity> possibleValueEntities = mapStringsToEntities(values);
        if (possibleValueEntities != null) {
            possibleValueEntities.forEach(pve -> pve.setFeatureEntity(featureEntity));
        }
        return possibleValueEntities;
    }


}
