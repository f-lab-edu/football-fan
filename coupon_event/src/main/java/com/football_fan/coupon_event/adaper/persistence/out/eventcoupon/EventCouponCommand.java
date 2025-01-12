package com.football_fan.coupon_event.adaper.persistence.out.eventcoupon;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;
import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;
import com.football_fan.coupon_event.adaper.persistence.out.jpa.DataJpaCoupon;
import com.football_fan.coupon_event.adaper.persistence.out.jpa.DataJpaEventCoupon;
import com.football_fan.coupon_event.application.domain.CouponEventType;
import com.football_fan.coupon_event.application.port.out.persistence.rdb.SaveEventCouponPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class EventCouponCommand implements SaveEventCouponPort {

    private final DataJpaEventCoupon eventCouponRepository;
    private final DataJpaCoupon couponRepository;

    @Override
    @Transactional
    public EventCoupon openNewEventCoupon(String eventName, CouponEventType couponEventType, int toBeIssued) {
        EventCoupon eventCoupon = EventCoupon.createEventCoupon(eventName, toBeIssued, couponEventType);
        return eventCouponRepository.save(eventCoupon);
    }

    public void saveEventCoupon(Long eventId, int couponCount) {
        EventCoupon eventCoupon = eventCouponRepository.findById(eventId).orElseThrow(
                () -> new IllegalArgumentException("Event not found")
        );
        eventCoupon.deliverCoupon(couponCount);
        eventCouponRepository.save(eventCoupon);
    }

    @Override
    public Coupon createNewCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public EventCoupon save(EventCoupon eventCoupon) {
        return eventCouponRepository.save(eventCoupon);
    }
}
