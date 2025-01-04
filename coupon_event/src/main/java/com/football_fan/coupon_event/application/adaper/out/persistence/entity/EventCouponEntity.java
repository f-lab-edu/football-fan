package com.football_fan.coupon_event.application.adaper.out.persistence.entity;

import com.football_fan.coupon_event.application.domain.CouponEventType;
import com.football_fan.coupon_event.application.domain.EventStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event_coupons")
public class EventCouponEntity {
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

    public void endEvent() {
        status = EventStatus.CLOSED;
        coupons.forEach(Coupon::makeCouponExpired);
    }

}
