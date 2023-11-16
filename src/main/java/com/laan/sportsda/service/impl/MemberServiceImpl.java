package com.laan.sportsda.service.impl;

import com.laan.sportsda.dto.request.MemberRegistrationRequest;
import com.laan.sportsda.dto.response.LoginResponse;
import com.laan.sportsda.dto.response.MemberRegistrationResponse;
import com.laan.sportsda.dto.response.MemberResponse;
import com.laan.sportsda.dto.response.MemberShortResponse;
import com.laan.sportsda.entity.*;
import com.laan.sportsda.exception.InvalidRequestException;
import com.laan.sportsda.mapper.MemberMapper;
import com.laan.sportsda.mapper.SessionMapper;
import com.laan.sportsda.repository.*;
import com.laan.sportsda.service.MemberService;
import com.laan.sportsda.util.JwtUtil;
import com.laan.sportsda.util.PropertyUtil;
import com.laan.sportsda.validator.MemberValidator;
import com.laan.sportsda.validator.SportValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;

    private final RoleRepository roleRepository;

    private final FacultyRepository facultyRepository;

    private final MemberRepository memberRepository;

    private final MemberValidator memberValidator;

    private final SportValidator sportValidator;

    private final JwtUtil jwtUtil;

    private final SessionMapper sessionMapper;

    private final SessionRepository sessionRepository;

    private final SportRepository sportRepository;

    private final PropertyUtil propertyUtil;

    @Override
    public MemberRegistrationResponse registerMember(final MemberRegistrationRequest memberRegistrationRequest) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUsername(memberRegistrationRequest.getUsername());
        memberValidator.validateDuplicateMemberEntity(optionalMemberEntity);

        MemberRegistrationResponse memberRegistrationResponse = null;
        if (optionalMemberEntity.isEmpty()) {
            Optional<RoleEntity> optionalRoleEntity = roleRepository.findByName(propertyUtil.getBasicRoleName());
            if (optionalRoleEntity.isPresent()) {
                Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findById(memberRegistrationRequest.getFacultyId());
                if (optionalFacultyEntity.isPresent()) {
                    RoleEntity roleEntity = optionalRoleEntity.get();
                    FacultyEntity facultyEntity = optionalFacultyEntity.get();
                    MemberEntity memberEntity = memberMapper.mapRegistrationRequestToEntity(memberRegistrationRequest, roleEntity, facultyEntity);

                    MemberEntity savedMemberEntity = memberRepository.save(memberEntity);
                    memberRegistrationResponse = memberMapper.mapEntityToRegistrationResponse(savedMemberEntity);
                } else {
                    String msg = String.format("Necessary faculty for the id: %s is not present. So can't register the member", memberRegistrationRequest.getFacultyId());
                    log.error("Error occurred. {}", msg);
                    throw new InvalidRequestException(msg);
                }
            } else {
                String msg = "necessary role is not present. So can't register the member";
                log.error("Error occurred. {}", msg);
                throw new InvalidRequestException(msg);
            }
        }
        return memberRegistrationResponse;
    }

    @Override
    public LoginResponse loginMember(final String username, final String userAgent, final String ipAddress) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUsername(username);

        LoginResponse loginResponse = null;
        if (optionalMemberEntity.isPresent()) {
            SessionEntity sessionEntity = createSessionEntity(userAgent, ipAddress, optionalMemberEntity.get());
            SessionEntity savedSessionEntity = sessionRepository.save(sessionEntity);
            Map<String, Object> claimsMap = createClaimsMap(optionalMemberEntity.get());
            String token = jwtUtil.generateToken(username, savedSessionEntity.getId(), savedSessionEntity.getLoginDateTime(), savedSessionEntity.getExpiryDateTime(), claimsMap);
            loginResponse = sessionMapper.mapEntityToLoginResponse(token);
        }
        return loginResponse;
    }

    @Override
    public List<MemberShortResponse> getMembers() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        return memberMapper.mapEntitiesToShortResponses(memberEntities);
    }

    @Override
    public MemberResponse getMember(final String id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        memberValidator.validateNonExistingMemberEntity(id, optionalMemberEntity);

        MemberResponse memberResponse = null;
        if (optionalMemberEntity.isPresent()) {
            memberResponse = memberMapper.mapEntityToResponse(optionalMemberEntity.get());
        }
        return memberResponse;
    }

    @Override
    public MemberResponse getMemberSelf(final String username) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUsername(username);
        memberValidator.validateNonExistingMemberEntity(username, optionalMemberEntity);

        MemberResponse memberResponse = null;
        if (optionalMemberEntity.isPresent()) {
            memberResponse = memberMapper.mapEntityToResponse(optionalMemberEntity.get());
        }
        return memberResponse;
    }

    @Override
    @Transactional
    public MemberResponse playSport(final String sportId, final String username) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUsername(username);
        memberValidator.validateNonExistingMemberEntity(username, optionalMemberEntity);

        Optional<SportEntity> optionalSportEntity = sportRepository.findById(sportId);
        sportValidator.validateNonExistingSportEntity(sportId, optionalSportEntity);

        MemberResponse memberResponse = null;
        if (optionalMemberEntity.isPresent() && optionalSportEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            SportEntity sportEntity = optionalSportEntity.get();

            List<SportEntity> sportEntities = memberEntity.getSportEntities();
            List<SportEntity> existingSportEntitiesById = sportEntities.stream().filter(se -> se.getId().equals(sportEntity.getId())).toList();

            if (existingSportEntitiesById.isEmpty()) { // only add if the sport is new
                sportEntities.add(sportEntity);
            }

            MemberEntity updatedMemberEntity = memberRepository.save(memberEntity);
            memberResponse = memberMapper.mapEntityToResponse(updatedMemberEntity);
        }
        return memberResponse;
    }

    private SessionEntity createSessionEntity(final String userAgent, final String ipAddress, MemberEntity memberEntity) {
        Date issuedAtDate = new Date();
        Date expirationDate = new Date( issuedAtDate.getTime() + (1000 * propertyUtil.getJwtExpirySeconds()) );

        return sessionMapper.mapLoginRequestToEntity(issuedAtDate, expirationDate, userAgent, ipAddress, memberEntity);
    }

    private Map<String, Object> createClaimsMap(final MemberEntity memberEntity) {
        Map<String, Object> map = new HashMap<>();
        RoleEntity roleEntity = memberEntity.getRoleEntity();
        if (roleEntity != null && roleEntity.getPermissionEntities() != null) {
            List<String> authorities = roleEntity.getPermissionEntities().stream()
                    .map(permissionEntity -> permissionEntity.getDescription().toString())
                    .collect(Collectors.toList());
            map.put("permissions", authorities);
        }
        return map;
    }

}
