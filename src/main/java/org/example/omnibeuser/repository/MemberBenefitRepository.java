package org.example.omnibeuser.repository;

import org.example.omnibeuser.entity.MemberBenefit;
import org.example.omnibeuser.entity.type.MemberBenefitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberBenefitRepository extends JpaRepository<MemberBenefit, Long> {

    List<MemberBenefit> findAllByBenefitId(Long benefitId);
    Page<MemberBenefit> findByMember_MemberId(Long memberId, Pageable pageable);
    List<MemberBenefit> findByBenefitIdAndStatusIn(Long benefitId, List<MemberBenefitStatus> statuses);
    boolean existsByBenefitId(Long benefitId);

}
