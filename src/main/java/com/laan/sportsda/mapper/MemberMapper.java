package com.laan.sportsda.mapper;

import com.laan.sportsda.dto.response.MemberResponse;
import com.laan.sportsda.entity.MemberEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberResponse mapEntityToResponse(MemberEntity memberEntity);
}
