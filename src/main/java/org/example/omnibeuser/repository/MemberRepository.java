package org.example.omnibeuser.repository;

import org.example.omnibeuser.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(Long memberId);
    Optional<Member> findByLoginId(String loginId);
    List<Member> findTop20ByLoginIdContaining(String loginId);

}
