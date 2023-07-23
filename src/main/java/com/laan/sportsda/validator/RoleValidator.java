package com.laan.sportsda.validator;

import com.laan.sportsda.entity.RoleEntity;
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
public class RoleValidator {

    private final MessageSource messageSource;

    public void validateNonExistingRoleEntity(String id, Optional<RoleEntity> optionalRoleEntity) {
        if (optionalRoleEntity.isEmpty()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.NO_ROLE_EXCEPTION, null, LocaleContextHolder.getLocale()), id);
            throw new ElementNotFoundException(msg);
        }
    }

    public void validateDuplicateRoleEntity(Optional<RoleEntity> optionalRoleEntity) {
        if (optionalRoleEntity.isPresent()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.DUPLICATE_ROLE_EXCEPTION, null, LocaleContextHolder.getLocale()), optionalRoleEntity.get().getName());
            throw new DuplicateElementException(msg);
        }
    }
}
