package com.laan.sportsda.config;

import com.laan.sportsda.enums.PermissionDescription;
import com.laan.sportsda.filter.AuthenticationFilter;
import com.laan.sportsda.util.PathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationFilter authenticationFilter;

    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        requestsCustomizer -> requestsCustomizer
                                .requestMatchers(PathUtil.INIT).permitAll()
                                .requestMatchers(PathUtil.MEMBERS + PathUtil.REGISTER, PathUtil.MEMBERS + PathUtil.LOGIN).permitAll()
                                .requestMatchers(HttpMethod.GET, PathUtil.FACULTIES).permitAll()

                                .requestMatchers(HttpMethod.GET, PathUtil.PERMISSIONS + PathUtil.ID_PLACEHOLDER).hasAuthority(PermissionDescription.GET_PERMISSION.toString())
                                .requestMatchers(HttpMethod.GET, PathUtil.PERMISSIONS).hasAuthority(PermissionDescription.GET_PERMISSIONS.toString())

                                .requestMatchers(HttpMethod.GET, PathUtil.ROLES).hasAuthority(PermissionDescription.GET_ROLES.toString())
                                .requestMatchers(HttpMethod.GET, PathUtil.ROLES + PathUtil.ID_PLACEHOLDER).hasAuthority(PermissionDescription.GET_ROLE.toString())
                                .requestMatchers(HttpMethod.POST, PathUtil.ROLES).hasAuthority(PermissionDescription.ADD_ROLE.toString())
                                .requestMatchers(HttpMethod.PUT, PathUtil.ROLES + PathUtil.ID_PLACEHOLDER).hasAuthority(PermissionDescription.UPDATE_ROLE.toString())
                                .requestMatchers(HttpMethod.DELETE, PathUtil.ROLES + PathUtil.ID_PLACEHOLDER).hasAuthority(PermissionDescription.DELETE_ROLE.toString())

                                .requestMatchers(HttpMethod.GET, PathUtil.FACULTIES + PathUtil.ID_PLACEHOLDER).hasAuthority(PermissionDescription.GET_FACULTY.toString())
                                .requestMatchers(HttpMethod.POST, PathUtil.FACULTIES).hasAuthority(PermissionDescription.ADD_FACULTY.toString())
                                .requestMatchers(HttpMethod.PUT, PathUtil.FACULTIES).hasAuthority(PermissionDescription.UPDATE_FACULTY.toString())
                                .requestMatchers(HttpMethod.DELETE, PathUtil.FACULTIES).hasAuthority(PermissionDescription.DELETE_FACULTY.toString())

                                .requestMatchers(HttpMethod.GET, PathUtil.DEPARTMENTS).hasAuthority(PermissionDescription.GET_DEPARTMENTS.toString())
                                .requestMatchers(HttpMethod.GET, PathUtil.DEPARTMENTS + PathUtil.ID_PLACEHOLDER).hasAuthority(PermissionDescription.GET_DEPARTMENT.toString())
                                .requestMatchers(HttpMethod.POST, PathUtil.DEPARTMENTS).hasAuthority(PermissionDescription.ADD_DEPARTMENT.toString())
                                .requestMatchers(HttpMethod.PUT, PathUtil.DEPARTMENTS).hasAuthority(PermissionDescription.UPDATE_DEPARTMENT.toString())
                                .requestMatchers(HttpMethod.DELETE, PathUtil.DEPARTMENTS).hasAuthority(PermissionDescription.DELETE_DEPARTMENT.toString())
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(
                        logoutConfigurer -> logoutConfigurer
                                .logoutUrl(PathUtil.MEMBERS + PathUtil.LOGOUT)
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
