package org.example.omnibeuser.service;

import org.example.omnibeuser.dto.MemberReqDto;
import org.example.omnibeuser.entity.Member;

public interface MemberService {

    Member createNormalMember(MemberReqDto.NormalSignup normalSignupReqDto);

}
