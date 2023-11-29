package com.laan.sportsda.converter;

import com.laan.sportsda.enums.FeatureValueType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class FeatureValueTypeConverter implements AttributeConverter<FeatureValueType, String> {

    @Override
    public String convertToDatabaseColumn(FeatureValueType featureValueType) {
        return featureValueType.name();
    }

    @Override
    public FeatureValueType convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return Stream.of(FeatureValueType.values())
                .filter(fvt -> fvt.name().equals(s))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
