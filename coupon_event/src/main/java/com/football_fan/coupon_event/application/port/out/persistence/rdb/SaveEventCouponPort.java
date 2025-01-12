package com.football_fan.coupon_event.application.port.out.persistence.rdb;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;
import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;
import com.football_fan.coupon_event.application.domain.CouponEventType;

public interface SaveEventCouponPort {
    EventCoupon openNewEventCoupon(String eventName, CouponEventType couponEventType, int toBeIssued);
    Coupon createNewCoupon(Coupon coupon);
    EventCoupon save(EventCoupon eventCoupon);
}
