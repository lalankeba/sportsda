package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.response.FacultyShortResponse;
import com.laan.sportsda.dto.response.MemberRegistrationResponse;
import com.laan.sportsda.dto.response.MemberResponse;
import com.laan.sportsda.dto.response.MemberShortResponse;
import com.laan.sportsda.entity.FacultyEntity;
import com.laan.sportsda.entity.MemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "faculty", source = "facultyEntity")
    @Mapping(target = "role", source = "roleEntity")
    @Mapping(target = "departments", source = "departmentEntities")
    MemberResponse mapEntityToResponse(MemberEntity memberEntity);

    @Mapping(target = "faculty", source = "facultyEntity")
    MemberRegistrationResponse mapEntityToRegistrationResponse(MemberEntity memberEntity);

    FacultyShortResponse mapEntityToFacultyShortResponse(FacultyEntity facultyEntity);

    List<MemberShortResponse> mapEntitiesToShortResponses(List<MemberEntity> memberEntities);

    MemberShortResponse mapEntityToShortResponse(MemberEntity memberEntity);
}
