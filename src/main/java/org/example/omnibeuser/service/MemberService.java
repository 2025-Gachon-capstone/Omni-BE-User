package org.example.omnibeuser.service;

import org.example.omnibeuser.dto.MemberReqDto;
import org.example.omnibeuser.entity.Member;

public interface MemberService {

    Member createNormalMember(MemberReqDto.NormalSignup normalSignupReqDto);
    Member createSponsorMember(MemberReqDto.SponsorSignup sponsorSignupDto);
    boolean verifyPassword(Long memberId, String password);
    Member updateMember(Long memberId, MemberReqDto.UpdateMember updateMemberDto);
    Member deleteMember(Long memberId);

}
