package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.response.LoginResponse;
import com.laan.sportsda.entity.MemberEntity;
import com.laan.sportsda.entity.SessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Date;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    @Mapping(target = "logoutDateTime", ignore = true)
    @Mapping(target = "loggedOut", constant = "false")
    @Mapping(target = "memberEntity", source = "existingMemberEntity")
    SessionEntity mapLoginRequestToEntity(Date loginDateTime, Date expiryDateTime, String userAgent, String ipAddress, MemberEntity existingMemberEntity);

    @Mapping(target = "tokenType", constant = "Bearer")
    LoginResponse mapEntityToLoginResponse(String token);
}
