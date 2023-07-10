package com.laan.sportsda.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessagesUtil {

    public static final String WELCOME_MESSAGE = "welcome.message";

    public static final String NO_FACULTY_EXCEPTION = "no.faculty.exception";
    public static final String DUPLICATE_FACULTY_EXCEPTION = "duplicate.faculty.exception";
    public static final String MANDATORY_FACULTY_NAME = "mandatory.faculty.name";
    public static final String INVALID_FACULTY_NAME_SIZE = "invalid.faculty.name.size";
    public static final String MANDATORY_FACULTY_ID = "mandatory.faculty.id";

    public static final String MANDATORY_VERSION = "mandatory.version";

    public static final String NO_DEPARTMENT_EXCEPTION = "no.department.exception";
    public static final String DUPLICATE_DEPARTMENT_EXCEPTION = "duplicate.department.exception";
    public static final String MANDATORY_DEPARTMENT_NAME = "mandatory.department.name";
    public static final String INVALID_DEPARTMENT_NAME_SIZE = "invalid.department.name.size";

    public static final String NO_SPORT_EXCEPTION = "no.sport.exception";
    public static final String DUPLICATE_SPORT_EXCEPTION = "duplicate.sport.exception";
    public static final String MANDATORY_SPORT_NAME = "mandatory.sport.name";
    public static final String INVALID_SPORT_NAME_SIZE = "invalid.sport.name.size";

    public static final String NO_FEATURE_EXCEPTION = "no.feature.exception";
    public static final String DUPLICATE_FEATURE_EXCEPTION = "duplicate.feature.exception";
    public static final String NO_FROM_VALUE_EXCEPTION = "no.from.value.exception";
    public static final String NO_TO_VALUE_EXCEPTION = "no.to.value.exception";
    public static final String NO_MEASUREMENT_EXCEPTION = "no.measurement.exception";
    public static final String NO_POSSIBLE_VALUES_EXCEPTION = "no.possible.values.exception";
    public static final String MANDATORY_FEATURE_NAME = "mandatory.feature.name";
    public static final String INVALID_FEATURE_NAME_SIZE = "invalid.feature.name.size";

    public static final String DUPLICATE_MEMBER_EXCEPTION = "duplicate.member.exception";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AnnotationSupported {
        private static final String START = "{";
        private static final String END = "}";
        public static final String INVALID_FACULTY_NAME_SIZE = START + MessagesUtil.INVALID_FACULTY_NAME_SIZE + END;
        public static final String MANDATORY_FACULTY_NAME = START + MessagesUtil.MANDATORY_FACULTY_NAME + END;
        public static final String MANDATORY_FACULTY_ID = START + MessagesUtil.MANDATORY_FACULTY_ID + END;

        public static final String MANDATORY_VERSION = START + MessagesUtil.MANDATORY_VERSION + END;

        public static final String MANDATORY_DEPARTMENT_NAME = START + MessagesUtil.MANDATORY_DEPARTMENT_NAME + END;
        public static final String INVALID_DEPARTMENT_NAME_SIZE = START + MessagesUtil.INVALID_DEPARTMENT_NAME_SIZE + END;

        public static final String MANDATORY_SPORT_NAME = START + MessagesUtil.MANDATORY_SPORT_NAME + END;
        public static final String INVALID_SPORT_NAME_SIZE = START + MessagesUtil.INVALID_SPORT_NAME_SIZE + END;

        public static final String MANDATORY_FEATURE_NAME = START + MessagesUtil.MANDATORY_FEATURE_NAME + END;
        public static final String INVALID_FEATURE_NAME_SIZE = START + MessagesUtil.INVALID_FEATURE_NAME_SIZE + END;
    }
}
