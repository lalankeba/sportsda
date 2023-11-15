package com.laan.sportsda.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathUtil {

    public static final String INIT = "/init";
    public static final String FACULTIES = "/faculties";
    public static final String DEPARTMENTS = "/departments";
    public static final String SPORTS = "/sports";
    public static final String FEATURES = "/features";
    public static final String MEMBERS = "/members";
    public static final String PERMISSIONS = "/permissions";
    public static final String ROLES = "/roles";

    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String MEMBER = "/member";
    public static final String CURRENT = "/current";
    public static final String PLAY_SPORT = "/play-sport";

    public static final String ID_PLACEHOLDER = "/{id}";

}
