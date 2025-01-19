package com.football_fan.coupon_event.application.port.in.usecase;

public interface CouponUseCase {
    int issueCoupon(Long eventId, String userId);
}
