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
                                .requestMatchers(PathUtil.MEMBERS +"/**").permitAll()
                                .requestMatchers(HttpMethod.GET, PathUtil.FACULTIES + "/{id}").hasAuthority(PermissionDescription.GET_FACULTY.toString())
                                .requestMatchers(HttpMethod.GET, PathUtil.FACULTIES).hasAuthority(PermissionDescription.GET_FACULTIES.toString())
                                .requestMatchers(HttpMethod.POST, PathUtil.FACULTIES).hasAuthority(PermissionDescription.ADD_FACULTY.toString())
                                .requestMatchers(HttpMethod.PUT, PathUtil.FACULTIES).hasAuthority(PermissionDescription.UPDATE_FACULTY.toString())
                                .requestMatchers(HttpMethod.DELETE, PathUtil.FACULTIES).hasAuthority(PermissionDescription.DELETE_FACULTY.toString())
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(
                        logoutConfigurer -> logoutConfigurer
                                .logoutUrl(PathUtil.MEMBERS + "/logout")
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
