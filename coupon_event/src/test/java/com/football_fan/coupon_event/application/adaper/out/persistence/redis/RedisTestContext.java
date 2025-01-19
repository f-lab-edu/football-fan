package com.football_fan.coupon_event.application.adaper.out.persistence.redis;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;
import org.springframework.data.redis.core.RedisTemplate;

import static com.football_fan.coupon_event.application.port.out.persistence.inmeomry.CouponOutputPort.COUPON_EVENT_KEY;

public class RedisTestContext {
    private RedisTemplate<String, String > redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void deleteAfterWorkingWithStrategyTemplate(Coupon coupon, RedisTemplateCallback callback) {
        try {
            callback.proceed(coupon);
        } finally {
            redisTemplate.opsForHash().delete(COUPON_EVENT_KEY, coupon.getEventCoupon().getName());
        }
    }
}

