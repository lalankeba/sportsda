package com.laan.sportsda.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class LoginRequest {

    private String username;

    @ToString.Exclude
    private String password;
}
