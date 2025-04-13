package org.example.omnibeuser.repository;

import org.example.omnibeuser.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
