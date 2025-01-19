package com.football_fan.coupon_event.application.port.out.persistence.rdb;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;
import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;
import com.football_fan.coupon_event.application.domain.CouponEventType;

import java.util.List;

public interface SaveEventCouponPort {
    EventCoupon openNewEventCoupon(String eventName, CouponEventType couponEventType, int toBeIssued);
    Coupon saveCoupon(Coupon coupon);
    EventCoupon save(EventCoupon eventCoupon);
    List<Coupon> bulkInsertCoupon(List<Coupon> coupons);
}
