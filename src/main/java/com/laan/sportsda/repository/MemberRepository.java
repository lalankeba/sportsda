package com.laan.sportsda.repository;

import com.laan.sportsda.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    Optional<MemberEntity> findByUsername(String username);

}
