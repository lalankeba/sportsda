package com.laan.sportsda.controller;

import com.laan.sportsda.dto.request.LoginRequest;
import com.laan.sportsda.dto.request.MemberRegistrationRequest;
import com.laan.sportsda.dto.request.MemberRoleUpdateRequest;
import com.laan.sportsda.dto.request.MemberUpdateRequest;
import com.laan.sportsda.dto.response.MemberRegistrationResponse;
import com.laan.sportsda.dto.response.MemberResponse;
import com.laan.sportsda.dto.response.MemberShortResponse;
import com.laan.sportsda.service.MemberService;
import com.laan.sportsda.util.JwtUtil;
import com.laan.sportsda.util.PathUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathUtil.MEMBERS)
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @PostMapping(PathUtil.REGISTER)
    public ResponseEntity<Object> register(@Valid @RequestBody MemberRegistrationRequest memberRegistrationRequest) {
        log.info("registering new member");
        MemberRegistrationResponse memberRegistrationResponse = memberService.registerMember(memberRegistrationRequest);
        log.info("registered new member");
        return new ResponseEntity<>(memberRegistrationResponse, HttpStatus.CREATED);
    }

    @PostMapping(PathUtil.LOGIN)
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        log.info("Login");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            String userAgent = httpServletRequest.getHeader(HttpHeaders.USER_AGENT);
            String ipAddress = httpServletRequest.getRemoteAddr();
            return new ResponseEntity<>(memberService.loginMember(loginRequest.getUsername(), userAgent, ipAddress), HttpStatus.OK);
        } else {
            log.warn("Login not successful");
            throw new BadCredentialsException("Invalid user");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getMembers() {
        log.info("Getting members");
        List<MemberShortResponse> memberShortResponses = memberService.getMembers();
        log.info("get members");
        return new ResponseEntity<>(memberShortResponses, HttpStatus.OK);
    }

    @GetMapping(PathUtil.MEMBER + PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> getMember(@PathVariable("id") String id) {
        log.info("Getting member");
        MemberResponse memberResponse = memberService.getMember(id);
        log.info("get member");
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    @GetMapping(PathUtil.CURRENT)
    public ResponseEntity<Object> getCurrentMember(HttpServletRequest httpServletRequest) {
        log.info("Getting current member");
        String token = jwtUtil.getTokenFromRequest(httpServletRequest);
        String username = jwtUtil.extractUsername(token);
        MemberResponse memberResponse = memberService.getCurrentMember(username);
        log.info("Get current member");
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    @PutMapping(PathUtil.CURRENT)
    public ResponseEntity<Object> updateCurrentMember(@Valid @RequestBody MemberUpdateRequest memberUpdateRequest, HttpServletRequest httpServletRequest) {
        log.info("Updating current member");
        String token = jwtUtil.getTokenFromRequest(httpServletRequest);
        String username = jwtUtil.extractUsername(token);
        MemberResponse memberResponse = memberService.updateCurrentMember(memberUpdateRequest, username);
        log.info("Update member");
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    @PatchMapping(PathUtil.MEMBER + PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> updateMemberRole(@PathVariable("id") String id, @Valid @RequestBody MemberRoleUpdateRequest memberRoleUpdateRequest, HttpServletRequest httpServletRequest) {
        log.info("Updating member role");
        String token = jwtUtil.getTokenFromRequest(httpServletRequest);
        String username = jwtUtil.extractUsername(token);
        MemberResponse memberResponse = memberService.updateMemberRole(id, memberRoleUpdateRequest, username);
        log.info("Update member role");
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    @PostMapping(PathUtil.CURRENT + PathUtil.PLAY_SPORT + PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> playSport(@PathVariable("id") String sportId, HttpServletRequest httpServletRequest) {
        log.info("Member plays sport: {}", sportId);
        String token = jwtUtil.getTokenFromRequest(httpServletRequest);
        String username = jwtUtil.extractUsername(token);
        MemberResponse memberResponse = memberService.playSport(sportId, username);
        log.info("Member played sport");
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }
}
