package com.football_fan.coupon_event.application.domain.policy;


import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;

public class WelcomeCouponStrategy implements CouponEventTypeStrategy {

    @Override
    public Coupon apply(String userId) {
        Coupon coupon = new Coupon();
        coupon.noExpirationDate();
        return coupon.assignUserId(userId);
    }
}
