package org.example.omnibeuser.client;

import org.example.omnibeuser.dto.CardReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cardClient", url = "${CARD_SERVER_ADDRESS}")
public interface CardClient {

    // 회원가입시 카드 생성 요청
    @PostMapping("/card/v1/cards")
    void createCard(@RequestBody CardReqDto.CreateCard card);

}
