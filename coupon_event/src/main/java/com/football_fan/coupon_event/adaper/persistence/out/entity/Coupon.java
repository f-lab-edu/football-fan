package com.football_fan.coupon_event.adaper.persistence.out.entity;

import com.football_fan.coupon_event.application.domain.policy.CouponEventTypeStrategy;
import com.football_fan.coupon_event.application.domain.DiscountType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "coupons")
public class Coupon {
    @Id
    Long id;

    @Embedded
    private DiscountType discountType;

    private Float discountValue;

    private LocalDateTime expirationDate;

    private Boolean isUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_coupon_id")
    private EventCoupon eventCoupon;

    @Column(nullable = false, updatable = false)
    private String userId;

    private final LocalDateTime createdAt = LocalDateTime.now();
    private final LocalDateTime updatedAt = LocalDateTime.now();

    public Coupon() {
    }

    public Coupon(LocalDateTime expirationDate){
        this.expirationDate = expirationDate;
    }

    public static Coupon createCoupon(CouponEventTypeStrategy couponEventTypeStrategy) {
        return couponEventTypeStrategy.apply();
    }

    public void assignEventCoupon(EventCoupon eventCoupon) {
        this.eventCoupon = eventCoupon;
    }

    public boolean useCoupon() {
        return isUsed = true;
    }

    public Coupon yearlyExpirationDate() {
        expirationDate = LocalDateTime.now().plusYears(1);
        return this;
    }

    public Long getEventCouponId() {
        return eventCoupon.getId();
    }

    public void assignExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
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
