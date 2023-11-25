package com.laan.sportsda.validator;

import com.laan.sportsda.entity.MemberEntity;
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
public class MemberValidator {

    private final MessageSource messageSource;

    public void validateDuplicateMemberEntity(Optional<MemberEntity> optionalMemberEntity) {
        if (optionalMemberEntity.isPresent()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.DUPLICATE_MEMBER_EXCEPTION, null, LocaleContextHolder.getLocale()), optionalMemberEntity.get().getUsername());
            throw new DuplicateElementException(msg);
        }
    }

    public void validateNonExistingMemberEntity(String id, Optional<MemberEntity> optionalMemberEntity) {
        if (optionalMemberEntity.isEmpty()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.NO_MEMBER_EXCEPTION, null, LocaleContextHolder.getLocale()), id);
            throw new ElementNotFoundException(msg);
        }
    }

    public void validateSelfChangingMemberEntity(String username, Optional<MemberEntity> optionalMemberEntity) {
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            if (memberEntity.getUsername().equals(username)) {
                String msg = String.format(messageSource.getMessage(MessagesUtil.SELF_CHANGING_MEMBER_EXCEPTION, null, LocaleContextHolder.getLocale()));
                throw new InvalidRequestException(msg);
            }
        }
    }
}
