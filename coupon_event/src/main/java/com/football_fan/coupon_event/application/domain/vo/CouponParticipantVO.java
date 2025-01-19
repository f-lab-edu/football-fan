package com.football_fan.coupon_event.application.domain.vo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CouponParticipantVO {
    private Long eventId;
    private String userId;
}
