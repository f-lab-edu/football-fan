package com.football_fan.coupon_event.application.domain.policy;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;

public class BirthDayCouponStrategy implements CouponEventTypeStrategy {

    @Override
    public Coupon apply(String userId) {
        // 1년의 유효기간을 가짐
        return new Coupon().yearlyExpirationDate().assignUserId(userId);
    }
}
