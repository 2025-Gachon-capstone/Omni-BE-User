package org.example.omnibeuser.service;

import org.example.omnibeuser.dto.MemberReqDto;
import org.example.omnibeuser.entity.Member;

public interface MemberService {

    Member createNormalMember(MemberReqDto.NormalSignup normalSignupReqDto);
    Member createSponsorMember(MemberReqDto.SponsorSignup sponsorSignupDto);
    boolean verifyPassword(String loginId, String password);
    Member updateMember(String loginId, MemberReqDto.UpdateMember updateMemberDto);
    Member deleteMember(String loginId);
    Long findMemberIdByLoginId(String loginId);
}
