package org.example.omnibeuser.converter;

import org.example.omnibeuser.dto.BenefitResDto;
import org.example.omnibeuser.dto.MemberBenefitResDto;
import org.example.omnibeuser.entity.MemberBenefit;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public class MemberBenefitConverter {

    public static MemberBenefitResDto.GetMemberBenefit toGetCardBenefit(MemberBenefit mb, BenefitResDto.GetBatchBenefit benefit) {
        return MemberBenefitResDto.GetMemberBenefit.builder()
                .memberBenefitId(mb.getMemberBenefitId())
                .benefitId(mb.getBenefitId())
                .title(benefit.getTitle())
                .sponsorName(benefit.getSponsorName())
                .discountRate(benefit.getDiscountRate())
                .updatedAt(mb.getUpdatedAt().toLocalDate())
                .endDate(benefit.getEndDate())
                .status(String.valueOf(mb.getStatus()))
                .targetProduct(benefit.getTargetProduct())
                .build();
    }

    public static MemberBenefitResDto.GetMemberBenefitPage toGetMemberBenefitPage(
            Page<MemberBenefit> memberBenefits,
            Map<Long, BenefitResDto.GetBatchBenefit> benefitMap
    ) {
        List<MemberBenefitResDto.GetMemberBenefit> converted = memberBenefits.stream()
                .filter(mb -> benefitMap.containsKey(mb.getBenefitId()))
                .map(mb -> toGetCardBenefit(mb, benefitMap.get(mb.getBenefitId())))
                .toList();

        return MemberBenefitResDto.GetMemberBenefitPage.builder()
                .memberBenefits(converted)
                .isFirst(memberBenefits.isFirst())
                .isLast(memberBenefits.isLast())
                .pageSize(memberBenefits.getTotalPages())
                .totalElements(memberBenefits.getTotalElements())
                .build();
    }

}
