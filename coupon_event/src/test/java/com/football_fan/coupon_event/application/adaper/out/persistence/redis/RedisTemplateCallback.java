package com.football_fan.coupon_event.application.adaper.out.persistence.redis;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;

public interface RedisTemplateCallback {
    void proceed(Coupon coupon);
}
