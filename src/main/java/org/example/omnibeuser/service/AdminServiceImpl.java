package org.example.omnibeuser.service;

import org.example.omnibeuser.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibeuser.common.apiPayload.exception.GeneralException;
import org.example.omnibeuser.dto.MemberResDto;
import org.example.omnibeuser.entity.Member;
import org.example.omnibeuser.entity.type.MemberStatus;
import org.example.omnibeuser.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;

    public AdminServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public MemberResDto.DeleteMemberForAdmin deleteMemberForAdmin(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_MEMBER));

        if (member.getStatus() == MemberStatus.INACTIVE) {
            throw new GeneralException(ErrorStatus._ALREADY_RESIGN);
        }

        member.setStatus(MemberStatus.INACTIVE);
        Member savedMember = memberRepository.save(member);

        return new MemberResDto.DeleteMemberForAdmin(savedMember.getMemberId(), savedMember.getLoginId(), savedMember.getStatus().name());
    }
}
