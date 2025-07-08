package org.example.omnibeuser.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.example.omnibeuser.client.SponsorClient;
import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibeuser.common.apiPayload.exception.GeneralException;
import org.example.omnibeuser.dto.BenefitResDto;
import org.example.omnibeuser.dto.MemberBenefitReqDto;
import org.example.omnibeuser.entity.Member;
import org.example.omnibeuser.entity.MemberBenefit;
import org.example.omnibeuser.entity.type.MemberBenefitStatus;
import org.example.omnibeuser.repository.MemberBenefitRepository;
import org.example.omnibeuser.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MemberBenefitServiceImpl implements MemberBenefitService {

    private final SponsorClient sponsorClient;
    private final MemberBenefitRepository memberBenefitRepository;
    private final MemberRepository memberRepository;

    public MemberBenefitServiceImpl(SponsorClient sponsorClient, MemberBenefitRepository memberBenefitRepository,
                                    MemberRepository memberRepository) {
        this.sponsorClient = sponsorClient;
        this.memberBenefitRepository = memberBenefitRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public List<Long> createMemberBenefit(MemberBenefitReqDto.CreateMemberBenefit createMemberBenefitDto) {

        // 1. 스폰서 서비스에서 혜택 정보 가져오기
        BenefitResDto.GetBenefit benefitDto;
        try {
            ApiResult<BenefitResDto.GetBenefit> apiResult = sponsorClient.getBenefit(createMemberBenefitDto.getBenefitId());
            benefitDto = apiResult.getResult();

            if (benefitDto == null) {
                log.warn("benefitDto가 null입니다!");
                throw new GeneralException(ErrorStatus._INVALID_BENEFIT_STATUS);
            }

            log.info("benefitDto: id={}, amount={}, status={}",
                    benefitDto.getBenefitId(), benefitDto.getAmount(), benefitDto.getBenefitStatus());

        } catch (FeignException e) {
            log.error("스폰서 서비스 호출 중 오류: {}", e.contentUTF8());
            throw new GeneralException(ErrorStatus._SPONSOR_SERVICE_ERROR);
        }

        // 2. 혜택 상태 검증
        String statusStr = benefitDto.getBenefitStatus();
        if ("DELETED".equals(statusStr) || "EXPIRED".equals(statusStr)) {
            throw new GeneralException(ErrorStatus._INVALID_BENEFIT_STATUS);
        }

        // 3. 혜택 수량 검증
        if (benefitDto.getAmount() < createMemberBenefitDto.getMemberIdList().size()) {
            throw new GeneralException(ErrorStatus._EXCEED_BENEFIT_AMOUNT);
        }

        // 4. 전체 카드혜택 조회 (모든 상태 포함)
        List<MemberBenefit> allMemberBenefits = memberBenefitRepository.findAllByBenefitId(benefitDto.getBenefitId());


        // 5. memberId → CardBenefit 맵핑
        Map<Long, MemberBenefit> memberIdToBenefitMap = allMemberBenefits.stream()
                .collect(Collectors.toMap(mb -> mb.getMember().getMemberId(), mb -> mb, (mb1, mb2) -> mb1));

        // 6. 기존 유효 상태 (ONGOING, BEFORE 등)인 멤버 추출
        Set<Long> existingValidMemberIds = allMemberBenefits.stream()
                .filter(mb -> mb.getStatus() != MemberBenefitStatus.DELETED &&
                        mb.getStatus() != MemberBenefitStatus.EXPIRED)
                .map(mb -> mb.getMember().getMemberId())
                .collect(Collectors.toSet());

        Set<Long> requestedMemberIds = new HashSet<>(createMemberBenefitDto.getMemberIdList());

        // 7. 삭제 대상: 기존 유효 멤버 중 요청에서 빠진 멤버
        Set<Long> toDelete = new HashSet<>(existingValidMemberIds);
        toDelete.removeAll(requestedMemberIds);

        for (Long memberId : toDelete) {
            MemberBenefit benefit = memberIdToBenefitMap.get(memberId);
            if (benefit != null && benefit.getStatus() != MemberBenefitStatus.DELETED &&
                    benefit.getStatus() != MemberBenefitStatus.EXPIRED) {
                benefit.setStatus(MemberBenefitStatus.DELETED);
            }
        }

        // 8. 추가 대상: 요청에만 포함된 멤버 (기존 유효 멤버가 아님)
        Set<Long> toAdd = new HashSet<>(requestedMemberIds);
        toAdd.removeAll(existingValidMemberIds);

        MemberBenefitStatus newStatus = MemberBenefitStatus.valueOf(statusStr);

        for (Long memberId : toAdd) {
            MemberBenefit existingDeleted = memberIdToBenefitMap.get(memberId);
            if (existingDeleted != null && existingDeleted.getStatus() == MemberBenefitStatus.DELETED) {
                existingDeleted.setStatus(newStatus);
            } else {
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_MEMBER));

                MemberBenefit newBenefit = MemberBenefit.builder()
                        .member(member)
                        .benefitId(benefitDto.getBenefitId())
                        .status(newStatus)
                        .build();

                member.getMemberBenefits().add(newBenefit);
                memberBenefitRepository.save(newBenefit);
            }
        }

        List<Long> finalMemberIds = memberBenefitRepository.findAllByBenefitId(benefitDto.getBenefitId()).stream()
                .filter(mb -> mb.getStatus() != MemberBenefitStatus.DELETED &&
                        mb.getStatus() != MemberBenefitStatus.EXPIRED)
                .map(mb -> mb.getMember().getMemberId())
                .collect(Collectors.toList());

        return finalMemberIds;
    }

    @Override
    public void syncMemberBenefit(List<MemberBenefitReqDto.SyncMemberBenefit> syncMemberBenefitList) {

        for (MemberBenefitReqDto.SyncMemberBenefit dto : syncMemberBenefitList) {
            MemberBenefitStatus newStatus = MemberBenefitStatus.valueOf(dto.getNewStatus());

            List<MemberBenefit> benefitsToUpdate = memberBenefitRepository.findByBenefitIdAndStatusIn(
                    dto.getBenefitId(),
                    List.of(MemberBenefitStatus.BEFORE, MemberBenefitStatus.ONGOING)
            );

            for (MemberBenefit mb : benefitsToUpdate) {
                mb.setStatus(newStatus);
                memberBenefitRepository.save(mb);
            }
        }
    }

    @Override
    public Boolean existsMemberBenefit(Long benefitId) {
        boolean exists = memberBenefitRepository.existsByBenefitId(benefitId);
        return exists;
    }

}
