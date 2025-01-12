package com.football_fan.coupon_event.application.service.coupon;

import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;
import com.football_fan.coupon_event.application.port.out.persistence.rdb.FindEventCouponPort;
import com.football_fan.coupon_event.application.port.out.persistence.rdb.SaveEventCouponPort;
import com.football_fan.coupon_event.application.service.RedisService;
import com.football_fan.coupon_event.application.service.exception.RedisLockAcquisitionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final RedisService redisService;
    private final FindEventCouponPort findEventCouponPort;
    private final SaveEventCouponPort saveEventCouponPort;

    public EventCoupon getEventInformation(Long eventId) {
        return findEventCouponPort.findEventById(eventId).orElseThrow(
                () -> new IllegalArgumentException("Event not found")
        );
    }



    public void updateEventCoupon(Long eventId, int couponCount) {
        String EVENT_COUPON_LOCK = "EVENT:LOCK:";
        String lockKey = EVENT_COUPON_LOCK + eventId;

        if (redisService.acquireLock(lockKey)) {
            throw new RedisLockAcquisitionException("Unable to acquire lock for event update.");
        }
        try {
                EventCoupon eventCoupon = findEventCouponPort.findEventById(eventId).orElseThrow(
                        () -> new IllegalArgumentException("Event not found")
                );
                eventCoupon.deliverCoupon(couponCount);
                saveEventCouponPort.save(eventCoupon);
        } finally {
            redisService.releaseLock(lockKey);
        }
    }
}
