package com.laan.sportsda.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegistrationResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private FacultyShortResponse faculty;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
}
