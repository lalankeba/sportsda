package com.laan.sportsda.validator;

import com.laan.sportsda.entity.PermissionEntity;
import com.laan.sportsda.exception.ElementNotFoundException;
import com.laan.sportsda.util.MessagesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PermissionValidator {

    private final MessageSource messageSource;

    public void validateNonExistingPermissionEntity(String id, Optional<PermissionEntity> optionalPermissionEntity) {
        if (optionalPermissionEntity.isEmpty()) {
            String msg = String.format(messageSource.getMessage(MessagesUtil.NO_PERMISSION_EXCEPTION, null, LocaleContextHolder.getLocale()), id);
            throw new ElementNotFoundException(msg);
        }
    }

    public void validateNonExistingPermissionEntities(final List<String> permissionIds, final List<PermissionEntity> permissionEntities) {
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<String> newPermissionIds = new ArrayList<>(permissionIds);
            List<String> existingPermissionIds = permissionEntities.stream().map(PermissionEntity::getId).toList();

            newPermissionIds.removeAll(existingPermissionIds);
            if (!newPermissionIds.isEmpty()) {
                String ids = String.join(", ", newPermissionIds);
                String msg = String.format(messageSource.getMessage(MessagesUtil.NO_PERMISSION_EXCEPTION, null, LocaleContextHolder.getLocale()), ids);
                throw new ElementNotFoundException(msg);
            }
        }
    }
}
