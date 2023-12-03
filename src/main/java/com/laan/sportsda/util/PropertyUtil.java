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

    @Value("${app.admin.role.name}")
    private String adminRoleName;

    @Value("${app.basic.role.name}")
    private String basicRoleName;

    @Value("${app.basic.faculty.name}")
    private String basicFacultyName;

    @Value("${country.api.base.url}")
    private String countryApiBaseUrl;

    @Value("${country.api.update.interval.days}")
    private Long countryApiUpdateInterval;
}
