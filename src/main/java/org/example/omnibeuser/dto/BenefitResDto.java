package org.example.omnibeuser.dto;

import lombok.Getter;

import java.time.LocalDate;

public class BenefitResDto {

    @Getter
    public static class GetBenefit{

        private Long benefitId;
        private int amount;
        private String benefitStatus;

    }

    @Getter
    public static class GetBatchBenefit{

        private Long benefitId;
        private String title;
        private String sponsorName;
        private Float discountRate;
        private LocalDate endDate;
        private int amount;
        private String targetProduct;

    }

}
