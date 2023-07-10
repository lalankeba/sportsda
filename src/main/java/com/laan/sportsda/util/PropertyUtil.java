package com.laan.sportsda.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
@Getter
public class PropertyUtil {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${jwt.expiration.seconds}")
    private Long jwtExpirySeconds;

}
