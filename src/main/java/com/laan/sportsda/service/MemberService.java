package com.laan.sportsda.service;

import com.laan.sportsda.dto.request.MemberRegistrationRequest;
import com.laan.sportsda.dto.response.LoginResponse;
import com.laan.sportsda.dto.response.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface MemberService {

    MemberResponse registerMember(final MemberRegistrationRequest memberRegistrationRequest);

    LoginResponse loginMember(final String username, final HttpServletRequest httpServletRequest);
}
