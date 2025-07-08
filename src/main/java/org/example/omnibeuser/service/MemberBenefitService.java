package org.example.omnibeuser.service;

import org.example.omnibeuser.dto.MemberBenefitReqDto;

import java.util.List;

public interface MemberBenefitService {

    List<Long> createMemberBenefit(MemberBenefitReqDto.CreateMemberBenefit createMemberBenefitDto);

}
