package org.example.omnibeuser.dto;

import lombok.*;
import org.example.omnibeuser.entity.Member;

public class MemberResDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NormalSignup {
        private Long memberId;
        private String memberName;
        private String role;

        public static NormalSignup from(Member member) {
            return new NormalSignup(member.getMemberId(),
                    member.getMemberName(),
                    member.getRole().toString()
            );
        }

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SponsorSignup {
        private Long memberId;
        private String memberName;
        private String role;

        public static SponsorSignup from(Member member) {
            return new SponsorSignup(member.getMemberId(),
                    member.getMemberName(),
                    member.getRole().toString()
            );
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login{
        private String memberId;
        private String role;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMember{
        private String loginId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteMember{
        private String loginId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetMemberId{
        private Long MemberId;
    }




}
