package org.example.omnibeuser.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class MemberReqDto {

    @Getter
    public static class NormalSignup{

        @NotBlank(message = "이름은 필수입니다.")
        private String name;

        @NotBlank(message = "아이디는 필수입니다.")
        private String loginId;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @NotBlank(message = "확인 비밀번호는 필수입니다.")
        private String eqPassword;

        @NotBlank(message = "카드 비밀번호는 필수입니다.")
        private String cardPassword;

    }

    @Getter
    public static class SponsorSignup{
        @NotBlank(message = "이름은 필수입니다.")
        private String name;

        @NotBlank(message = "아이디는 필수입니다.")
        private String loginId;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @NotBlank(message = "확인 비밀번호는 필수입니다.")
        private String eqPassword;

        @NotBlank(message = "협찬사 이름은 필수입니다.")
        private String sponsorName;

        @NotBlank(message = "사업자 번호는 필수입니다.")
        private String sponsorNumber;

        @NotNull(message = "카테고리는 필수입니다.")
        private Long categoryId;

    }

    @Getter
    public static class Login{

        @NotBlank(message = "아이디는 필수입니다.")
        private String loginId;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

    }

    @Getter
    public static class VerifyPassword{

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

    }

    @Getter
    public static class UpdateMember{

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @NotBlank(message = "새로운 비밀번호는 필수입니다.")
        private String newPassword;

        @NotBlank(message = "새로운 확인 비밀번호는 필수입니다.")
        private String eqNewPassword;

    }

}
