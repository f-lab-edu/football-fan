package com.football_fan.coupon_event.application.domain.policy;


import com.football_fan.coupon_event.application.adaper.out.persistence.entity.Coupon;

public class WelcomeCouponStrategy implements CouponEventTypeStrategy {

    @Override
    public Coupon apply() {
        Coupon coupon = new Coupon();
        coupon.noExpirationDate();
        return coupon;
    }
}
