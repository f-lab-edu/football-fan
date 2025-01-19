package com.football_fan.coupon_event.application.port.out.persistence.inmeomry;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;

import java.time.Duration;

public interface InMemorySaveOutputPort {
    void increaseAmountOfCouponCount(Coupon coupon, int count, Long couponEventId, Duration duration);
}
