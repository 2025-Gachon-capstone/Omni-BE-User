package org.example.omnibeuser.dto;

import lombok.Getter;

public class MemberReqDto {

    @Getter
    public static class NormalSignup{

        String name;
        String loginId;
        String password;
        String eqPassword;
        String cardPassword;

    }

    @Getter
    public static class SponsorSignup{
        String name;
        String loginId;
        String password;
        String eqPassword;
        String sponsorName;
        String sponsorNumber;
        Long categoryId;

    }

    @Getter
    public static class Login{

        String loginId;
        String password;

    }

    @Getter
    public static class VerifyPassword{

        String password;

    }

    @Getter
    public static class UpdateMember{

        String password;
        String newPassword;
        String eqNewPassword;

    }

}
