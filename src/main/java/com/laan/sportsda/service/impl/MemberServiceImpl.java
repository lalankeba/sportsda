package com.laan.sportsda.service.impl;

import com.laan.sportsda.dto.request.MemberRegistrationRequest;
import com.laan.sportsda.dto.response.LoginResponse;
import com.laan.sportsda.dto.response.MemberResponse;
import com.laan.sportsda.entity.*;
import com.laan.sportsda.mapper.custom.MemberMapperCustom;
import com.laan.sportsda.mapper.SessionMapper;
import com.laan.sportsda.repository.DepartmentRepository;
import com.laan.sportsda.repository.MemberRepository;
import com.laan.sportsda.repository.RoleRepository;
import com.laan.sportsda.repository.SessionRepository;
import com.laan.sportsda.service.MemberService;
import com.laan.sportsda.util.JwtUtil;
import com.laan.sportsda.util.PropertyUtil;
import com.laan.sportsda.validator.MemberValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberMapperCustom memberMapperCustom;

    private final RoleRepository roleRepository;

    private final DepartmentRepository departmentRepository;

    private final MemberRepository memberRepository;

    private final MemberValidator memberValidator;

    private final JwtUtil jwtUtil;

    private final SessionMapper sessionMapper;

    private final SessionRepository sessionRepository;

    private final PropertyUtil propertyUtil;

    @Override
    public MemberResponse registerMember(final MemberRegistrationRequest memberRegistrationRequest) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUsername(memberRegistrationRequest.getUsername());
        memberValidator.validateDuplicateMemberEntity(optionalMemberEntity);

        MemberResponse memberResponse = null;
        if (optionalMemberEntity.isEmpty()) {
            Optional<RoleEntity> optionalRoleEntity = roleRepository.findByName("ADMIN");
            if (optionalRoleEntity.isPresent()) {
                Optional<DepartmentEntity> optionalDepartmentEntity = departmentRepository.findById(memberRegistrationRequest.getDepartmentId());
                if (optionalDepartmentEntity.isPresent()) {
                    RoleEntity roleEntity = optionalRoleEntity.get();
                    DepartmentEntity departmentEntity = optionalDepartmentEntity.get();
                    MemberEntity memberEntity = memberMapperCustom.mapRegistrationRequestToEntity(memberRegistrationRequest, roleEntity, departmentEntity);

                    MemberEntity savedMemberEntity = memberRepository.save(memberEntity);
                    memberResponse = memberMapperCustom.mapEntityToResponse(savedMemberEntity);
                }
            }
        }
        return memberResponse;
    }

    @Override
    public LoginResponse loginMember(final String username, final HttpServletRequest httpServletRequest) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUsername(username);

        LoginResponse loginResponse = null;
        if (optionalMemberEntity.isPresent()) {
            SessionEntity sessionEntity = createSessionEntity(httpServletRequest, optionalMemberEntity.get());
            SessionEntity savedSessionEntity = sessionRepository.save(sessionEntity);
            Map<String, Object> claimsMap = createClaimsMap(optionalMemberEntity.get());
            String token = jwtUtil.generateToken(username, savedSessionEntity.getId(), savedSessionEntity.getLoginDateTime(), savedSessionEntity.getExpiryDateTime(), claimsMap);
            loginResponse = sessionMapper.mapEntityToLoginResponse(token);
        }
        return loginResponse;
    }

    private SessionEntity createSessionEntity(final HttpServletRequest httpServletRequest, MemberEntity memberEntity) {
        String userAgent = httpServletRequest.getHeader(HttpHeaders.USER_AGENT);
        String ipAddress = httpServletRequest.getRemoteAddr();
        Date issuedAtDate = new Date();
        Date expirationDate = new Date( issuedAtDate.getTime() + (1000 * propertyUtil.getJwtExpirySeconds()) );

        return sessionMapper.mapLoginRequestToEntity(issuedAtDate, expirationDate, userAgent, ipAddress, memberEntity);
    }

    private Map<String, Object> createClaimsMap(final MemberEntity memberEntity) {
        Map<String, Object> map = new HashMap<>();
        RoleEntity roleEntity = memberEntity.getRoleEntity();
        if (roleEntity != null && roleEntity.getPermissionEntities() != null) {
            List<String> authorities = roleEntity.getPermissionEntities().stream()
                    .map(permissionEntity -> new String(permissionEntity.getDescription().toString()))
                    .collect(Collectors.toList());
            map.put("permissions", authorities);
        }
        return map;
    }

}
