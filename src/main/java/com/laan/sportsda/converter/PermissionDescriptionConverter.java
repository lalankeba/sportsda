package com.laan.sportsda.converter;

import com.laan.sportsda.enums.PermissionDescription;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class PermissionDescriptionConverter implements AttributeConverter<PermissionDescription, String> {

    @Override
    public String convertToDatabaseColumn(PermissionDescription permissionDescription) {
        return permissionDescription.name();
    }

    @Override
    public PermissionDescription convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return Stream.of(PermissionDescription.values())
                .filter(pd -> pd.name().equals(s))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
