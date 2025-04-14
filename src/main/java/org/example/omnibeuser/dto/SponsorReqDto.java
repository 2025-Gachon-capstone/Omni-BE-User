package org.example.omnibeuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SponsorReqDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateSponsor{
        private Long memberId;
        private String sponsorName;
        private String sponsorNumber;
        private Long categoryId;

        public static CreateSponsor from(MemberReqDto.SponsorSignup dto,Long memberId) {
            return new CreateSponsor(
                    memberId,
                    dto.getSponsorName(),
                    dto.getSponsorNumber(),
                    dto.getCategoryId()
            );
        }

    }

}
