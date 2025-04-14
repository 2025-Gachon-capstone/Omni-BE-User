package org.example.omnibeuser.converter;

import org.example.omnibeuser.dto.MemberReqDto;
import org.example.omnibeuser.entity.Member;
import org.example.omnibeuser.entity.type.MemberStatus;
import org.example.omnibeuser.entity.type.Role;

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
}
