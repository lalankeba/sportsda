package com.laan.sportsda.validator;

import com.laan.sportsda.entity.MemberEntity;
import com.laan.sportsda.exception.DuplicateElementException;
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
}
