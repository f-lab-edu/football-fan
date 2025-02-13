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

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class EventCouponCommand implements SaveEventCouponPort {

    private final DataJpaEventCoupon eventCouponRepository;
    private final DataJpaCoupon couponRepository;

    @Override
    public EventCoupon openNewEventCoupon(String eventName, CouponEventType couponEventType, int toBeIssued) {
        return eventCouponRepository.save(EventCoupon.createEventCoupon(eventName, toBeIssued, couponEventType));
    }

    @Override
    public Coupon saveCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> bulkInsertCoupon(List<Coupon> coupons) {
        return couponRepository.saveAll(coupons);
    }

    @Override
    public EventCoupon save(EventCoupon eventCoupon) {
        return eventCouponRepository.save(eventCoupon);
    }
}
