package com.football_fan.coupon_event.adaper.persistence.out.redis;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;
import com.football_fan.coupon_event.application.port.out.persistence.inmeomry.CouponOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepository implements CouponOutputPort {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void increaseAmountOfCouponCount(Coupon coupon, int count,  Long couponEventId, Duration eventExpirationDate) {
        redisTemplate.opsForHash().increment(COUPON_EVENT_KEY, coupon.getEventCoupon().getName(), count);
        redisTemplate.expire(COUPON_EVENT_KEY, eventExpirationDate);
    }

    @Override
    public <T> T find(String key, Class<T> type) {
        try {
            Integer value = (Integer) redisTemplate.opsForHash().get(COUPON_EVENT_KEY, key);
            int count = Optional.ofNullable(value).orElse(0); // null이면 기본값 0 사용

            return type.cast(count);
        } catch (ClassCastException | SerializationException e) {
            throw new RuntimeException("Failed to deserialize value for key: " + key, e);
        }
    }
}
