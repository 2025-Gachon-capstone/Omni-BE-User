package org.example.omnibeuser.client;

import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.dto.BenefitResDto;
import org.example.omnibeuser.dto.SponsorReqDto;
import org.example.omnibeuser.dto.SponsorResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "sponsorClient", url = "${SPONSOR_SERVER_ADDRESS}")
public interface SponsorClient {

    // 회원가입시 스폰서 생성 요청
    @PostMapping("/sponsor/v1/sponsors")
    void createSponsor(@RequestBody SponsorReqDto.CreateSponsor sponsor);

    // 스폰서 id 가져오기
    @GetMapping("/sponsor/v1/sponsors/{memberId}")
    ApiResult<SponsorResDto.GetSponsorId> getSponsorId(@PathVariable("memberId") Long memberId);

    @GetMapping("/sponsor/v1/benefits/{benefitId}")
    ApiResult<BenefitResDto.GetBenefit> getBenefit(@PathVariable("benefitId") Long benefitId);

    @PostMapping("/sponsor/v1/benefits/batch")
    ApiResult<List<BenefitResDto.GetBatchBenefit>> getBatchBenefits(@RequestBody List<Long> benefitIds);

}
