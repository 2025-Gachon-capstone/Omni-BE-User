package org.example.omnibeuser.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class MemberBenefitReqDto {

    @Getter
    public static class CreateMemberBenefit{

        @NotNull(message = "benefitId는 필수입니다.")
        private Long benefitId;

        @NotNull(message = "memberIdList는 필수입니다.")
        private List<Long> memberIdList;

    }
}
