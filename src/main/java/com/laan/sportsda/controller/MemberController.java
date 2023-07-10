package com.laan.sportsda.controller;

import com.laan.sportsda.dto.request.LoginRequest;
import com.laan.sportsda.dto.request.MemberRegistrationRequest;
import com.laan.sportsda.dto.response.MemberResponse;
import com.laan.sportsda.service.MemberService;
import com.laan.sportsda.util.PathUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathUtil.MEMBERS)
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody MemberRegistrationRequest memberRegistrationRequest) {
        log.info("registering new member");
        MemberResponse memberResponse = memberService.registerMember(memberRegistrationRequest);
        log.info("registered new member");
        return new ResponseEntity<>(memberResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        log.info("Login");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(memberService.loginMember(loginRequest.getUsername(), httpServletRequest), HttpStatus.OK);
        } else {
            log.warn("Login not successful");
            throw new BadCredentialsException("Invalid user");
        }
    }
}
