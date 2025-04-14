package org.example.omnibeuser.client;

import org.example.omnibeuser.dto.SponsorReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sponsorClient", url = "${SPONSOR_SERVER_ADDRESS}")
public interface SponsorClient {

    // 회원가입시 스폰서 생성 요청
    @PostMapping("/sponsor/v1/sponsors")
    void createSponsor(@RequestBody SponsorReqDto.CreateSponsor sponsor);

}
