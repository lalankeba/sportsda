package com.laan.sportsda.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String username;

    @ToString.Exclude
    private String password;
}
