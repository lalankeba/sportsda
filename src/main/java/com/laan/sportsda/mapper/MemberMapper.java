package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.request.MemberRegistrationRequest;
import com.laan.sportsda.dto.response.*;
import com.laan.sportsda.entity.*;
import com.laan.sportsda.security.MemberDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {FacultyMapper.class, SportMapper.class})
public abstract class MemberMapper {

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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

    @Mapping(target = "faculty", source = "facultyEntity")
    @Mapping(target = "role", source = "roleEntity")
    @Mapping(target = "departments", source = "departmentEntities")
    @Mapping(target = "sports", source = "sportEntities")
    public abstract MemberResponse mapEntityToResponse(MemberEntity memberEntity);

    @Mapping(target = "faculty", source = "facultyEntity")
    public abstract MemberRegistrationResponse mapEntityToRegistrationResponse(MemberEntity memberEntity);

    public abstract List<MemberShortResponse> mapEntitiesToShortResponses(List<MemberEntity> memberEntities);

    public abstract MemberShortResponse mapEntityToShortResponse(MemberEntity memberEntity);

}
