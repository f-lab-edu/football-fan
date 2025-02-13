package com.football_fan.coupon_event.application.service.coupon;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;
import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;
import com.football_fan.coupon_event.application.domain.policy.FirstComeCouponStrategy;
import com.football_fan.coupon_event.application.port.out.persistence.rdb.FindEventCouponPort;
import com.football_fan.coupon_event.application.port.out.persistence.rdb.SaveEventCouponPort;
import com.football_fan.coupon_event.application.service.RedisService;
import com.football_fan.coupon_event.application.service.exception.RedisLockAcquisitionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon.createCoupon;


@Service
@RequiredArgsConstructor
@Transactional
public class CouponIssueService {

    private final String EVENT_COUPON_LOCK = "EVENT:LOCK:";
    private final RedisService redisService;
    private final FindEventCouponPort findEventCouponPort;
    private final SaveEventCouponPort saveEventCouponPort;

    public EventCoupon getEventInformation(Long eventId) {
        return findEventCouponPort.findEventById(eventId).orElseThrow(
                () -> new IllegalArgumentException("Event not found")
        );
    }

    public void createAndSaveCoupons(List<String> userIds, EventCoupon eventCoupon) {
        List<Coupon> coupons = userIds.stream().map(userId -> createCoupon(userId, eventCoupon)).collect(Collectors.toList());
        saveEventCouponPort.bulkInsertCoupon(coupons);
    }

    private Coupon createCoupon(String userId, EventCoupon eventCoupon) {
        return Coupon.createCoupon(userId, new FirstComeCouponStrategy(LocalDateTime.now().plusDays(1), eventCoupon));
    }

    public void updateEventCoupon(Long eventId, int couponCount) {

        String lockKey = getEventCouponLockKey(eventId);

        if (!redisService.acquireLock(lockKey)) {
            throw new RedisLockAcquisitionException("Unable to acquire lock for event update.");
        }
        try {
            EventCoupon eventCoupon = findEventCouponPort.findEventById(eventId).orElseThrow(
                    () -> new IllegalArgumentException("Event not found")
            );
            eventCoupon.validateCoupon();
            eventCoupon.deliverCoupon(couponCount);
            saveEventCouponPort.save(eventCoupon);
        } finally {
            redisService.releaseLock(lockKey);
        }
    }

    private String getEventCouponLockKey(Long eventId) {
        return EVENT_COUPON_LOCK + eventId;
    }
}
