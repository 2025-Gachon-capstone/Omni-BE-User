package org.example.omnibeuser.repository;

import org.example.omnibeuser.entity.MemberBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberBenefitRepository extends JpaRepository<MemberBenefit, Long> {

    List<MemberBenefit> findAllByBenefitId(Long benefitId);
    boolean existsByBenefitId(Long benefitId);

}
