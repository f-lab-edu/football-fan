package com.football_fan.coupon_event.application.port.out.persistence.rdb;

import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;

import java.util.Optional;

public interface FindEventCouponPort {
    Optional<EventCoupon> findEventById(Long id);
}
