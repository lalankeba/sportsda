package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.response.FacultyShortResponse;
import com.laan.sportsda.dto.response.MemberRegistrationResponse;
import com.laan.sportsda.dto.response.MemberResponse;
import com.laan.sportsda.entity.FacultyEntity;
import com.laan.sportsda.entity.MemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberResponse mapEntityToResponse(MemberEntity memberEntity);

    @Mapping(target = "faculty", source = "facultyEntity")
    MemberRegistrationResponse mapEntityToRegistrationResponse(MemberEntity memberEntity);

    FacultyShortResponse mapEntityToFacultyShortResponse(FacultyEntity facultyEntity);
}
