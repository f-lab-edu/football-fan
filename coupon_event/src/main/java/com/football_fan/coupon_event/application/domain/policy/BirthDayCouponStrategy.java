package com.football_fan.coupon_event.application.domain.policy;

import com.football_fan.coupon_event.application.adaper.out.persistence.entity.Coupon;

public class BirthDayCouponStrategy implements CouponEventTypeStrategy {

    @Override
    public Coupon apply() {
        // 1년의 유효기간을 가짐
        return new Coupon().yearlyExpirationDate();
    }
}
