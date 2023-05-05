package com.laan.sportsda.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MessagesUtil {

    public static final String WELCOME_MESSAGE = "welcome.message";

    public static final String NO_FACULTY_EXCEPTION = "no.faculty.exception";
    public static final String DUPLICATE_FACULTY_EXCEPTION = "duplicate.faculty.exception";
    public static final String MANDATORY_FACULTY_NAME = "mandatory.faculty.name";
    public static final String INVALID_FACULTY_NAME_SIZE = "invalid.faculty.name.size";

    public static final String NO_DEPARTMENT_EXCEPTION = "no.department.exception";
    public static final String DUPLICATE_DEPARTMENT_EXCEPTION = "duplicate.department.exception";

    public static final String NO_SPORT_EXCEPTION = "no.sport.exception";
    public static final String DUPLICATE_SPORT_EXCEPTION = "duplicate.sport.exception";
}
