package com.laan.sportsda.validator;

import com.laan.sportsda.dto.request.FeatureAddRequest;
import com.laan.sportsda.dto.request.FeatureUpdateRequest;
import com.laan.sportsda.entity.FeatureEntity;
import com.laan.sportsda.enums.FeatureValueType;
import com.laan.sportsda.exception.DuplicateElementException;
import com.laan.sportsda.exception.ElementNotFoundException;
import com.laan.sportsda.exception.InvalidRequestException;
import com.laan.sportsda.util.MessagesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FeatureValidator {

    private final MessageSource messageSource;

    public void validateNonExistingFeatureEntity(String id, Optional<FeatureEntity> optionalFeatureEntity) {
        if (optionalFeatureEntity.isEmpty()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.NO_FEATURE_EXCEPTION, null, LocaleContextHolder.getLocale()), id);
            throw new ElementNotFoundException(msg);
        }
    }

    public void validateDuplicateFeatureEntity(Optional<FeatureEntity> optionalFeatureEntity, String sportName) {
        if (optionalFeatureEntity.isPresent()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.DUPLICATE_FEATURE_EXCEPTION, null, LocaleContextHolder.getLocale()), optionalFeatureEntity.get().getName(), sportName);
            throw new DuplicateElementException(msg);
        }
    }

    public void validateFeatureAddRequestWithTypeAndValues(FeatureAddRequest featureAddRequest) {
        validateAttributesOnValueType(featureAddRequest.getFeatureValueType(), featureAddRequest.getMinValue(), featureAddRequest.getMaxValue(), featureAddRequest.getMeasurement(), featureAddRequest.getPossibleValues());
    }

    public void validateFeatureUpdateRequestWithTypeAndValues(FeatureUpdateRequest featureUpdateRequest) {
        validateAttributesOnValueType(featureUpdateRequest.getFeatureValueType(), featureUpdateRequest.getMinValue(), featureUpdateRequest.getMaxValue(), featureUpdateRequest.getMeasurement(), featureUpdateRequest.getPossibleValues());
    }

    private void validateAttributesOnValueType(FeatureValueType featureValueType, String minValue, String maxValue, String measurement, List<String> possibleValues) {
        if (featureValueType == FeatureValueType.DECIMAL || featureValueType == FeatureValueType.INTEGER) {
            if (minValue == null) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.NO_MIN_VALUE_EXCEPTION, null, LocaleContextHolder.getLocale()), featureValueType);
                throw new InvalidRequestException(msg);
            } else if (maxValue == null) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.NO_MAX_VALUE_EXCEPTION, null, LocaleContextHolder.getLocale()), featureValueType);
                throw new InvalidRequestException(msg);
            } else if (measurement == null) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.NO_MEASUREMENT_EXCEPTION, null, LocaleContextHolder.getLocale()), featureValueType);
                throw new InvalidRequestException(msg);
            } else if (possibleValues != null && !possibleValues.isEmpty()) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.INVALID_ATTRIBUTES_FOR_VALUE_TYPE_EXCEPTION, null, LocaleContextHolder.getLocale()), featureValueType, "possibleValues");
                throw new InvalidRequestException(msg);
            }
        } else if (featureValueType == FeatureValueType.FIXED_VALUE) {
            if (possibleValues == null || possibleValues.isEmpty()) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.NO_POSSIBLE_VALUES_EXCEPTION, null, LocaleContextHolder.getLocale()));
                throw new InvalidRequestException(msg);
            } else if (minValue != null) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.INVALID_ATTRIBUTES_FOR_VALUE_TYPE_EXCEPTION, null, LocaleContextHolder.getLocale()), featureValueType, "minValue");
                throw new InvalidRequestException(msg);
            } else if (maxValue != null) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.INVALID_ATTRIBUTES_FOR_VALUE_TYPE_EXCEPTION, null, LocaleContextHolder.getLocale()), featureValueType, "maxValue");
                throw new InvalidRequestException(msg);
            } else if (measurement != null) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.INVALID_ATTRIBUTES_FOR_VALUE_TYPE_EXCEPTION, null, LocaleContextHolder.getLocale()), featureValueType, "measurement");
                throw new InvalidRequestException(msg);
            }
        }
    }

}
