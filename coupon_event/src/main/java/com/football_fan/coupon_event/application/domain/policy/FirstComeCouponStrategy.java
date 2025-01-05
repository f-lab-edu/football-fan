package com.football_fan.coupon_event.application.domain.policy;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;
import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;

import java.time.LocalDateTime;

public class FirstComeCouponStrategy implements CouponEventTypeStrategy{

    private final LocalDateTime expire;
    private final EventCoupon eventCoupon;

    public FirstComeCouponStrategy(LocalDateTime expire, EventCoupon eventCoupon) {
        this.expire =  expire;
        this.eventCoupon = eventCoupon;
    }

    @Override
    public Coupon apply() {
        Coupon coupon = new Coupon();
        coupon.assignExpirationDate(expire);
        coupon.assignEventCoupon(eventCoupon);
        return coupon;
    }
}
