package org.example.omnibeuser.dto;

import lombok.*;

public class MemberResDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NormalSignup {
        private Long memberId;
        private String memberName;
    }
}
