package com.laan.sportsda.mapper.custom;

import com.laan.sportsda.dto.request.MemberRegistrationRequest;
import com.laan.sportsda.dto.response.MemberRegistrationResponse;
import com.laan.sportsda.dto.response.MemberResponse;
import com.laan.sportsda.dto.response.MemberShortResponse;
import com.laan.sportsda.entity.*;
import com.laan.sportsda.mapper.MemberMapper;
import com.laan.sportsda.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemberMapperCustom {

    private final PasswordEncoder passwordEncoder;

    private final MemberMapper memberMapper;

    public MemberDetails mapEntityToDetails(MemberEntity memberEntity) {
        if ( memberEntity == null ) {
            return null;
        }
        String username = memberEntity.getUsername();
        String password = memberEntity.getPassword();
        List<GrantedAuthority> authorities = null;
        boolean accountNonExpired = false;
        boolean accountNonLocked = false;
        boolean credentialsNonExpired = false;
        boolean enabled = false;

        RoleEntity roleEntity = memberEntity.getRoleEntity();
        if (roleEntity != null) {
            authorities = mapPermissionEntitiesToAuthorities(roleEntity.getPermissionEntities());
        }
        if ( memberEntity.getAccountNonExpired() != null ) {
            accountNonExpired = memberEntity.getAccountNonExpired();
        }
        if ( memberEntity.getAccountNonLocked() != null ) {
            accountNonLocked = memberEntity.getAccountNonLocked();
        }
        if ( memberEntity.getCredentialsNonExpired() != null ) {
            credentialsNonExpired = memberEntity.getCredentialsNonExpired();
        }
        if ( memberEntity.getEnabled() != null ) {
            enabled = memberEntity.getEnabled();
        }

        return new MemberDetails( username, password, authorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled );
    }

    private List<GrantedAuthority> mapPermissionEntitiesToAuthorities(List<PermissionEntity> permissionEntities) {
        if (permissionEntities == null) {
            return Collections.emptyList();
        }
        return permissionEntities
                .stream()
                .map(permissionEntity -> new SimpleGrantedAuthority(permissionEntity.getDescription().toString()))
                .map(GrantedAuthority.class::cast)
                .toList();
    }

    public MemberDetails mapJwtToDetails(String username, List<String> permissions) {
        List<GrantedAuthority> authorities = mapPermissionStringsToAuthorities(permissions);
        return new MemberDetails(username, null, authorities, true, true, true, true);
    }

    private List<GrantedAuthority> mapPermissionStringsToAuthorities(List<String> permissions) {
        if (permissions == null) {
            return Collections.emptyList();
        }
        return permissions
                .stream()
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();
    }

    public MemberEntity mapRegistrationRequestToEntity(MemberRegistrationRequest memberRegistrationRequest, RoleEntity roleEntity, FacultyEntity facultyEntity) {
        if (memberRegistrationRequest == null) {
            return null;
        }
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUsername(memberRegistrationRequest.getUsername());
        memberEntity.setPassword(passwordEncoder.encode(memberRegistrationRequest.getPassword()));
        memberEntity.setFirstName(memberRegistrationRequest.getFirstName());
        memberEntity.setLastName(memberRegistrationRequest.getLastName());
        memberEntity.setAccountNonExpired(true);
        memberEntity.setAccountNonLocked(true);
        memberEntity.setCredentialsNonExpired(true);
        memberEntity.setEnabled(true);
        memberEntity.setRoleEntity(roleEntity);
        memberEntity.setFacultyEntity(facultyEntity);

        return memberEntity;
    }

    public MemberResponse mapEntityToResponse(MemberEntity memberEntity) {
        return memberMapper.mapEntityToResponse(memberEntity);
    }

    public MemberRegistrationResponse mapEntityToRegistrationResponse(MemberEntity memberEntity) {
        return memberMapper.mapEntityToRegistrationResponse(memberEntity);
    }

    public List<MemberShortResponse> mapEntitiesToShortResponses(List<MemberEntity> memberEntities) {
        return memberMapper.mapEntitiesToShortResponses(memberEntities);
    }
}
