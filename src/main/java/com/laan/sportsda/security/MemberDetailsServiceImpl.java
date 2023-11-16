package com.laan.sportsda.security;

import com.laan.sportsda.entity.MemberEntity;
import com.laan.sportsda.mapper.MemberMapper;
import com.laan.sportsda.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUsername(username);
        if (optionalMemberEntity.isPresent()) {
            return memberMapper.mapEntityToDetails(optionalMemberEntity.get());
        } else {
            throw new UsernameNotFoundException("User cannot be found for: " + username);
        }
    }
}
