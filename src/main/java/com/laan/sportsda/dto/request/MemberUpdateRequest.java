package com.laan.sportsda.dto.request;

import com.laan.sportsda.util.MessagesUtil;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MemberUpdateRequest {

    @NotBlank(message = MessagesUtil.AnnotationSupported.MANDATORY_MEMBER_FIRST_NAME)
    @Size(min = 2, max = 40, message = MessagesUtil.AnnotationSupported.INVALID_MEMBER_FIRST_NAME_SIZE)
    private String firstName;

    @Size(min = 2, max = 40, message = MessagesUtil.AnnotationSupported.INVALID_MEMBER_MIDDLE_NAME_SIZE)
    private String middleName;

    @NotBlank(message = MessagesUtil.AnnotationSupported.MANDATORY_MEMBER_LAST_NAME)
    @Size(min = 2, max = 40, message = MessagesUtil.AnnotationSupported.INVALID_MEMBER_LAST_NAME_SIZE)
    private String lastName;

    private Date dateOfBirth;

    @Pattern(regexp = "^([0-9]{9}[vVxX]|[0-9]{12})$", message = MessagesUtil.AnnotationSupported.INVALID_MEMBER_NIC_FORMAT)
    private String nic;

    @Size(min = 10, max = 15, message = MessagesUtil.AnnotationSupported.INVALID_MEMBER_PHONE_NUMBER_SIZE)
    private String phone;

    @Size(min = 2, max = 40, message = MessagesUtil.AnnotationSupported.INVALID_MEMBER_EMAIL_SIZE)
    @Email(message = MessagesUtil.AnnotationSupported.INVALID_MEMBER_EMAIL_FORMAT)
    private String universityEmail;

    @Size(min = 2, max = 40, message = MessagesUtil.AnnotationSupported.INVALID_MEMBER_EMAIL_SIZE)
    @Email(message = MessagesUtil.AnnotationSupported.INVALID_MEMBER_EMAIL_FORMAT)
    private String personalEmail;

    @Size(min = 2, max = 100, message = MessagesUtil.AnnotationSupported.INVALID_MEMBER_ADDRESS_SIZE)
    private String address;

    @Size(min = 3, max = 30, message = MessagesUtil.AnnotationSupported.INVALID_MEMBER_DISTRICT_SIZE)
    private String district;

    @NotBlank(message = MessagesUtil.AnnotationSupported.MANDATORY_FACULTY_ID)
    private String facultyId;

    private List<String> departmentIds;

}
