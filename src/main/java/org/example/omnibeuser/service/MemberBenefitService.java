package org.example.omnibeuser.service;

import org.example.omnibeuser.dto.MemberBenefitReqDto;
import org.example.omnibeuser.dto.MemberBenefitResDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberBenefitService {

    List<Long> createMemberBenefit(MemberBenefitReqDto.CreateMemberBenefit createMemberBenefitDto);
    MemberBenefitResDto.GetMemberBenefitPage getMemberBenefits(Long memberId, Pageable pageable);
    void syncMemberBenefit(List<MemberBenefitReqDto.SyncMemberBenefit> syncMemberBenefitList);
    Boolean existsMemberBenefit(Long benefitId);


}
