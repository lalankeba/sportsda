package com.laan.sportsda.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class MemberRegistrationRequest {

    @NotBlank
    @Size(min = 2, max = 40)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 40)
    private String lastName;

    @NotBlank
    @Size(min = 2, max = 40)
    @Email
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    @ToString.Exclude
    private String password;

    @NotBlank
    private String facultyId;
}
