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
    public static final String INVALID_DEPARTMENTS_FOR_FACULTY_EXCEPTION = "invalid.departments.for.faculty.exception";

    public static final String NO_SPORT_EXCEPTION = "no.sport.exception";
    public static final String DUPLICATE_SPORT_EXCEPTION = "duplicate.sport.exception";
    public static final String MANDATORY_SPORT_NAME = "mandatory.sport.name";
    public static final String INVALID_SPORT_NAME_SIZE = "invalid.sport.name.size";

    public static final String NO_FEATURE_EXCEPTION = "no.feature.exception";
    public static final String DUPLICATE_FEATURE_EXCEPTION = "duplicate.feature.exception";
    public static final String NO_MIN_VALUE_EXCEPTION = "no.min.value.exception";
    public static final String NO_MAX_VALUE_EXCEPTION = "no.max.value.exception";
    public static final String NO_MEASUREMENT_EXCEPTION = "no.measurement.exception";
    public static final String INVALID_ATTRIBUTES_FOR_VALUE_TYPE_EXCEPTION = "invalid.attributes.for.value.type.exception";
    public static final String NO_POSSIBLE_VALUES_EXCEPTION = "no.possible.values.exception";
    public static final String MANDATORY_FEATURE_NAME = "mandatory.feature.name";
    public static final String INVALID_FEATURE_NAME_SIZE = "invalid.feature.name.size";

    public static final String DUPLICATE_MEMBER_EXCEPTION = "duplicate.member.exception";
    public static final String NO_MEMBER_EXCEPTION = "no.member.exception";
    public static final String SELF_CHANGING_MEMBER_EXCEPTION = "self.changing.member.exception";

    public static final String NO_PERMISSION_EXCEPTION = "no.permission.exception";

    public static final String NO_ROLE_EXCEPTION = "no.role.exception";
    public static final String DUPLICATE_ROLE_EXCEPTION = "duplicate.role.exception";
    public static final String MANDATORY_ROLE_NAME = "mandatory.role.name";
    public static final String INVALID_ROLE_NAME_SIZE = "invalid.role.name.size";
    public static final String MANDATORY_ROLE_DESCRIPTION = "mandatory.role.description";
    public static final String INVALID_ROLE_DESCRIPTION_SIZE = "invalid.role.description.size";
    public static final String MANDATORY_ROLE_ID = "mandatory.role.id";

    public static final String MANDATORY_MEMBER_FIRST_NAME = "mandatory.member.first.name";
    public static final String INVALID_MEMBER_FIRST_NAME_SIZE = "invalid.member.first.name.size";
    public static final String INVALID_MEMBER_MIDDLE_NAME_SIZE = "invalid.member.middle.name.size";
    public static final String MANDATORY_MEMBER_LAST_NAME = "mandatory.member.last.name";
    public static final String INVALID_MEMBER_LAST_NAME_SIZE = "invalid.member.last.name.size";
    public static final String INVALID_MEMBER_NIC_FORMAT = "invalid.member.nic.format";
    public static final String INVALID_MEMBER_PHONE_NUMBER_SIZE = "invalid.member.phone.number.size";
    public static final String INVALID_MEMBER_EMAIL_SIZE = "invalid.member.email.size";
    public static final String INVALID_MEMBER_EMAIL_FORMAT = "invalid.member.email.format";
    public static final String INVALID_MEMBER_ADDRESS_SIZE = "invalid.member.address.size";
    public static final String INVALID_MEMBER_DISTRICT_SIZE = "invalid.member.district.size";

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

        public static final String MANDATORY_ROLE_NAME = START + MessagesUtil.MANDATORY_ROLE_NAME + END;
        public static final String INVALID_ROLE_NAME_SIZE = START + MessagesUtil.INVALID_ROLE_NAME_SIZE + END;
        public static final String MANDATORY_ROLE_DESCRIPTION = START + MessagesUtil.MANDATORY_ROLE_DESCRIPTION + END;
        public static final String INVALID_ROLE_DESCRIPTION_SIZE = START + MessagesUtil.INVALID_ROLE_DESCRIPTION_SIZE + END;
        public static final String MANDATORY_ROLE_ID = START + MessagesUtil.MANDATORY_ROLE_ID + END;

        public static final String MANDATORY_MEMBER_FIRST_NAME = START + MessagesUtil.MANDATORY_MEMBER_FIRST_NAME + END;
        public static final String INVALID_MEMBER_FIRST_NAME_SIZE = START + MessagesUtil.INVALID_MEMBER_FIRST_NAME_SIZE + END;
        public static final String INVALID_MEMBER_MIDDLE_NAME_SIZE = START + MessagesUtil.INVALID_MEMBER_MIDDLE_NAME_SIZE + END;
        public static final String MANDATORY_MEMBER_LAST_NAME = START + MessagesUtil.MANDATORY_MEMBER_LAST_NAME + END;
        public static final String INVALID_MEMBER_LAST_NAME_SIZE = START + MessagesUtil.INVALID_MEMBER_LAST_NAME_SIZE + END;
        public static final String INVALID_MEMBER_NIC_FORMAT = START + MessagesUtil.INVALID_MEMBER_NIC_FORMAT + END;
        public static final String INVALID_MEMBER_PHONE_NUMBER_SIZE = START + MessagesUtil.INVALID_MEMBER_PHONE_NUMBER_SIZE + END;
        public static final String INVALID_MEMBER_EMAIL_SIZE = START + MessagesUtil.INVALID_MEMBER_EMAIL_SIZE + END;
        public static final String INVALID_MEMBER_EMAIL_FORMAT = START + MessagesUtil.INVALID_MEMBER_EMAIL_FORMAT + END;
        public static final String INVALID_MEMBER_ADDRESS_SIZE = START + MessagesUtil.INVALID_MEMBER_ADDRESS_SIZE + END;
        public static final String INVALID_MEMBER_DISTRICT_SIZE = START + MessagesUtil.INVALID_MEMBER_DISTRICT_SIZE + END;
    }
}
