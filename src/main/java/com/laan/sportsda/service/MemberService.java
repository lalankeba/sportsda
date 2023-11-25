package com.laan.sportsda.service;

import com.laan.sportsda.dto.request.MemberRegistrationRequest;
import com.laan.sportsda.dto.request.MemberRoleUpdateRequest;
import com.laan.sportsda.dto.request.MemberUpdateRequest;
import com.laan.sportsda.dto.response.LoginResponse;
import com.laan.sportsda.dto.response.MemberRegistrationResponse;
import com.laan.sportsda.dto.response.MemberResponse;
import com.laan.sportsda.dto.response.MemberShortResponse;

import java.util.List;

public interface MemberService {

    MemberRegistrationResponse registerMember(final MemberRegistrationRequest memberRegistrationRequest);

    LoginResponse loginMember(final String username, final String userAgent, final String ipAddress);

    List<MemberShortResponse> getMembers();

    MemberResponse getMember(final String id);

    MemberResponse getCurrentMember(String username);

    MemberResponse updateCurrentMember(MemberUpdateRequest memberUpdateRequest, String username);

    MemberResponse playSport(final String sportId, final String username);

    MemberResponse updateMemberRole(String memberId, MemberRoleUpdateRequest memberRoleUpdateRequest, String currentUsername);
}
