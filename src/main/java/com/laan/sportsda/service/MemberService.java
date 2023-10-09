package com.laan.sportsda.service;

import com.laan.sportsda.dto.request.MemberRegistrationRequest;
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

    MemberResponse getMemberSelf(final String username);
}
