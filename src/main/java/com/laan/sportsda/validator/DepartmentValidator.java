package com.laan.sportsda.validator;

import com.laan.sportsda.entity.DepartmentEntity;
import com.laan.sportsda.exception.DuplicateElementException;
import com.laan.sportsda.exception.ElementNotFoundException;
import com.laan.sportsda.util.MessagesUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DepartmentValidator {

    private final MessageSource messageSource;

    public DepartmentValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void validateNonExistingDepartmentEntity(String id, Optional<DepartmentEntity> optionalDepartmentEntity) {
        if (optionalDepartmentEntity.isEmpty()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.NO_DEPARTMENT_EXCEPTION, null, LocaleContextHolder.getLocale()), id);
            throw new ElementNotFoundException(msg);
        }
    }

    public void validateDuplicateDepartmentEntity(Optional<DepartmentEntity> optionalDepartmentEntity) {
        if (optionalDepartmentEntity.isPresent()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.DUPLICATE_DEPARTMENT_EXCEPTION, null, LocaleContextHolder.getLocale()), optionalDepartmentEntity.get().getName());
            throw new DuplicateElementException(msg);
        }
    }

}
