package org.example.omnibeuser.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.example.omnibeuser.client.CardClient;
import org.example.omnibeuser.client.SponsorClient;
import org.example.omnibeuser.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibeuser.common.apiPayload.exception.GeneralException;
import org.example.omnibeuser.converter.MemberConverter;
import org.example.omnibeuser.dto.CardReqDto;
import org.example.omnibeuser.dto.MemberReqDto;
import org.example.omnibeuser.dto.SponsorReqDto;
import org.example.omnibeuser.entity.Member;
import org.example.omnibeuser.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {


    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final CardClient cardClient;
    private final SponsorClient sponsorClient;

    public MemberServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, MemberRepository memberRepository,
                             CardClient cardClient, SponsorClient sponsorClient) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.memberRepository = memberRepository;
        this.cardClient = cardClient;
        this.sponsorClient = sponsorClient;
    }

    @Override
    @Transactional
    public Member createNormalMember(MemberReqDto.NormalSignup normalSignupReqDto) {

        if(!normalSignupReqDto.getPassword().equals(normalSignupReqDto.getEqPassword())) {
            throw new GeneralException(ErrorStatus._NOT_MATCH_PASSWORD);
        }

        // 비밀번호 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(normalSignupReqDto.getPassword());

        Member member = MemberConverter.toMember(normalSignupReqDto, encodedPassword);

        Member savedMember = memberRepository.save(member);

        // 카드 서비스 통신 오류시 회원가입 롤백
        try {
            cardClient.createCard(new CardReqDto.CreateCard(savedMember.getMemberId(), savedMember.getMemberName(), normalSignupReqDto.getCardPassword()));
        } catch (FeignException e) {
            log.error("카드 서비스 호출 중 오류: {}", e.contentUTF8());
            throw new GeneralException(ErrorStatus._CARD_CREATION_FAILED);
        }

        return savedMember;
    }

    @Override
    @Transactional
    public Member createSponsorMember(MemberReqDto.SponsorSignup sponsorSignupDto) {

        if(!sponsorSignupDto.getPassword().equals(sponsorSignupDto.getEqPassword())) {
            throw new GeneralException(ErrorStatus._NOT_MATCH_PASSWORD);
        }

        // 비밀번호 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(sponsorSignupDto.getPassword());

        Member member = MemberConverter.toMember(sponsorSignupDto, encodedPassword);

        Member savedMember = memberRepository.save(member);

        try {
            SponsorReqDto.CreateSponsor dto = SponsorReqDto.CreateSponsor.from(sponsorSignupDto, savedMember.getMemberId());
            sponsorClient.createSponsor(dto);
        } catch (FeignException e) {
            log.error("스폰서 서비스 호출 중 오류: {}", e.contentUTF8());
            throw new GeneralException(ErrorStatus._SPONSOR_CREATION_FAILED);
        }

        return savedMember;
    }
}
