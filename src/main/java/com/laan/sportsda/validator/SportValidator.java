package com.laan.sportsda.validator;

import com.laan.sportsda.entity.SportEntity;
import com.laan.sportsda.exception.DuplicateElementException;
import com.laan.sportsda.exception.ElementNotFoundException;
import com.laan.sportsda.util.MessagesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SportValidator {

    private final MessageSource messageSource;

    public void validateNonExistingSportEntity(String id, Optional<SportEntity> optionalSportEntity) {
        if (optionalSportEntity.isEmpty()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.NO_SPORT_EXCEPTION, null, LocaleContextHolder.getLocale()), id);
            throw new ElementNotFoundException(msg);
        }
    }

    public void validateDuplicateSportEntity(Optional<SportEntity> optionalSportEntity) {
        if (optionalSportEntity.isPresent()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.DUPLICATE_SPORT_EXCEPTION, null, LocaleContextHolder.getLocale()), optionalSportEntity.get().getName());
            throw new DuplicateElementException(msg);
        }
    }
}
