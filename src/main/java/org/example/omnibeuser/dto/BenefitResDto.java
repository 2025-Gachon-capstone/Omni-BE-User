package org.example.omnibeuser.dto;

import lombok.Getter;

public class BenefitResDto {

    @Getter
    public static class GetBenefit{

        private Long benefitId;
        private int amount;
        private String benefitStatus;

    }

}
