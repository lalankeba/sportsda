package com.laan.sportsda.validator;

import com.laan.sportsda.entity.FacultyEntity;
import com.laan.sportsda.exception.DuplicateElementException;
import com.laan.sportsda.exception.ElementNotFoundException;
import com.laan.sportsda.util.MessagesUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FacultyValidator {

    private final MessageSource messageSource;

    public FacultyValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void validateNonExistingFacultyEntity(String id, Optional<FacultyEntity> optionalFacultyEntity) {
        if (optionalFacultyEntity.isEmpty()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.NO_FACULTY_EXCEPTION, null, LocaleContextHolder.getLocale()), id);
            throw new ElementNotFoundException(msg);
        }
    }

    public void validateDuplicateFacultyEntity(Optional<FacultyEntity> optionalFacultyEntity) {
        if (optionalFacultyEntity.isPresent()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.DUPLICATE_FACULTY_EXCEPTION, null, LocaleContextHolder.getLocale()), optionalFacultyEntity.get().getName());
            throw new DuplicateElementException(msg);
        }
    }
}
