package com.laan.sportsda.dto.response;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String tokenType;

}
