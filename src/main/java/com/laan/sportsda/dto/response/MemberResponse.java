package com.laan.sportsda.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MemberResponse {

    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String username;
    private Date dateOfBirth;
    private String nic;
    private String phone;
    private String universityEmail;
    private String personalEmail;
    private String address;
    private String district;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    private FacultyShortResponse faculty;
    private RoleShortResponse role;
    private List<DepartmentShortResponse> departments;
    private List<SportShortResponse> sports;
}
