package com.laan.sportsda.validator;

import com.laan.sportsda.dto.request.FeatureAddRequest;
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
        if (featureAddRequest.getFeatureValueType() == FeatureValueType.DECIMAL || featureAddRequest.getFeatureValueType() == FeatureValueType.INTEGER) {
            if (featureAddRequest.getFromValue() == null) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.NO_FROM_VALUE_EXCEPTION, null, LocaleContextHolder.getLocale()));
                throw new InvalidRequestException(msg);
            } else if (featureAddRequest.getToValue() == null) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.NO_TO_VALUE_EXCEPTION, null, LocaleContextHolder.getLocale()));
                throw new InvalidRequestException(msg);
            } else if (featureAddRequest.getMeasurement() == null) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.NO_MEASUREMENT_EXCEPTION, null, LocaleContextHolder.getLocale()));
                throw new InvalidRequestException(msg);
            }
        } else if (featureAddRequest.getFeatureValueType() == FeatureValueType.FIXED_VALUE && (featureAddRequest.getPossibleValues() == null || featureAddRequest.getPossibleValues().isEmpty())) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.NO_POSSIBLE_VALUES_EXCEPTION, null, LocaleContextHolder.getLocale()));
            throw new InvalidRequestException(msg);
        }
    }
}
