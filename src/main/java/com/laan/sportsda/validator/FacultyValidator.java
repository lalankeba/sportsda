package com.laan.sportsda.validator;

import com.laan.sportsda.entity.DepartmentEntity;
import com.laan.sportsda.entity.FacultyEntity;
import com.laan.sportsda.exception.DuplicateElementException;
import com.laan.sportsda.exception.ElementNotFoundException;
import com.laan.sportsda.exception.InvalidRequestException;
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
public class FacultyValidator {

    private final MessageSource messageSource;

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

    public void validateFacultyEntityWithCorrectDepartmentEntities(final FacultyEntity facultyEntity, final List<String> departmentIds) {
        List<String> departmentIdsInFaculty = facultyEntity.getDepartmentEntities()
                .stream().map(DepartmentEntity::getId).toList();

        List<String> departmentIdsDiff = new ArrayList<>(departmentIds);
        departmentIdsDiff.removeAll(departmentIdsInFaculty);

        if (!departmentIdsDiff.isEmpty()) {
            String unmatchedIds = String.join(", ", departmentIdsDiff);
            String msg = String.format(messageSource.getMessage(MessagesUtil.INVALID_DEPARTMENTS_FOR_FACULTY_EXCEPTION, null, LocaleContextHolder.getLocale()), unmatchedIds, facultyEntity.getName());
            throw new InvalidRequestException(msg);
        }
    }
}
