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
    public static class UserLogin{
        private Long memberId;
        private String loginId;
        private String memberName;
        private String role;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SponsorLogin{
        private Long memberId;
        private Long sponsorId;
        private String loginId;
        private String memberName;
        private String role;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMember{
        private Long memberId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteMember{
        private Long memberId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetMemberId{
        private Long MemberId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetMemberList{

        private Long memberId;
        private String memberName;
        private String loginId;
        private String status;

    }


}
