package com.football_fan.coupon_event.application.domain.policy;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;

public interface CouponEventTypeStrategy {
    Coupon apply();
}
