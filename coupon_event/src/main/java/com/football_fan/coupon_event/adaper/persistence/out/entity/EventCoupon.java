package com.football_fan.coupon_event.adaper.persistence.out.entity;

import com.football_fan.coupon_event.application.domain.CouponEventType;
import com.football_fan.coupon_event.application.domain.EventStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "event_coupons")
public class EventCoupon {
    @Id
    private Long id;

    private String name;
    private int issuedCount;
    private int usedCount;

    @Embedded
    private CouponEventType couponEventType;
    private EventStatus status;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Coupon> coupons = new ArrayList<>();

    public void addCoupon(Coupon coupon) {
        coupons.add(coupon);
    }

    public static EventCoupon createEventCoupon(String name, int issuedCount, CouponEventType couponEventType) {
        EventCoupon eventCoupon = new EventCoupon();
        eventCoupon.name = name;
        eventCoupon.issuedCount = issuedCount;
        eventCoupon.couponEventType = couponEventType;
        eventCoupon.status = EventStatus.OPENED;
        return eventCoupon;
    }

    public void endEvent() {
        status = EventStatus.CLOSED;
        coupons.forEach(Coupon::makeCouponExpired);
    }

    public boolean isClosed() {
        return status == EventStatus.CLOSED;
    }

    public boolean isFull() {
        return issuedCount == usedCount;
    }

    public int deliverCoupon(int numberOfCoupons) {
        int totalCouponCount = usedCount + numberOfCoupons;
        if (totalCouponCount >= issuedCount) {
            endEvent();
            usedCount = issuedCount;
            // return exceeded coupon count
            return totalCouponCount - issuedCount;
        }
        usedCount = totalCouponCount;
        return 0;
    }

    public void validateCoupon() {
        if (isClosed()) {
            throw new IllegalArgumentException("Invalid coupon");
        }
    }
}
