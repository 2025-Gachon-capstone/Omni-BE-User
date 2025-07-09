package org.example.omnibeuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class MemberBenefitResDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetMemberBenefit{

        private Long memberBenefitId;
        private Long benefitId;
        private String title;
        private String sponsorName;
        private Float discountRate;
        private LocalDate updatedAt;
        private LocalDate endDate;
        private String status;
        private String targetProduct;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetMemberBenefitPage{

        List<MemberBenefitResDto.GetMemberBenefit> memberBenefits;
        boolean isFirst;
        boolean isLast;
        int pageSize;
        long totalElements;

    }

}
