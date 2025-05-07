package org.example.omnibeuser.converter;

import org.example.omnibeuser.dto.MemberReqDto;
import org.example.omnibeuser.dto.MemberResDto;
import org.example.omnibeuser.entity.Member;
import org.example.omnibeuser.entity.type.MemberStatus;
import org.example.omnibeuser.entity.type.Role;

import java.util.List;

public class MemberConverter {

    public static Member toMember(MemberReqDto.NormalSignup dto, String encodedPassword) {
        return Member.builder()
                .memberName(dto.getName())
                .loginId(dto.getLoginId())
                .password(encodedPassword)
                .role(Role.USER)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    public static Member toMember(MemberReqDto.SponsorSignup dto, String encodedPassword) {
        return Member.builder()
                .memberName(dto.getName())
                .loginId(dto.getLoginId())
                .password(encodedPassword)
                .role(Role.SPONSOR)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    public static MemberResDto.GetMemberList getMemberList(Member member) {
        return MemberResDto.GetMemberList.builder()
                .memberName(member.getMemberName())
                .loginId(member.getLoginId())
                .build();
    }

}
