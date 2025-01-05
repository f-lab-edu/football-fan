package com.football_fan.coupon_event.application.port.out.persistence.inmeomry;

public interface CouponOutputPort extends InMemoryFindOutputPort, InMemorySaveOutputPort {
    static String COUPON_EVENT_KEY = "coupon_event";
}
