package com.football_fan.coupon_event.application.adaper.out.persistence.entity;

import com.football_fan.coupon_event.application.domain.policy.CouponEventTypeStrategy;
import com.football_fan.coupon_event.application.domain.DiscountType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    Long id;

    @Embedded
    DiscountType discountType;

    Float discountValue;

    LocalDateTime expirationDate;
    private Boolean isUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_coupon_id")
    EventCouponEntity eventCoupon;

    @Column(nullable = false, updatable = false)
    String userId;

    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();

    public Coupon() {
    }

    public Coupon(LocalDateTime expirationDate){
        this.expirationDate = expirationDate;
    }

    public static Coupon createCoupon(CouponEventTypeStrategy couponEventTypeStrategy) {
        return couponEventTypeStrategy.apply();
    }

    public boolean useCoupon() {
        return isUsed = true;
    }

    public Coupon yearlyExpirationDate() {
        expirationDate = LocalDateTime.now().plusYears(1);
        return this;
    }

    public void noExpirationDate() {
        expirationDate = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
    }

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }

    public void makeCouponExpired() {
        expirationDate = LocalDateTime.now().minusSeconds(1);
    }
}
