package com.football_fan.coupon_event.adaper.persistence.out.entity;

import com.football_fan.coupon_event.application.domain.CouponEventType;
import com.football_fan.coupon_event.application.domain.policy.FirstComeCouponStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventCouponTest {

    @Test
    @DisplayName("이벤트가 닫힌 상태일 때 이벤트 상태를 확인하면 예외가 발생해야 한다.")
    void shouldThrowExceptionWhenValidatingEventStatusAfterClosure() {
        // given
        EventCoupon eventCoupon = EventCoupon.createEventCoupon(
                "testEvent",
                10,
                CouponEventType.FIRST_COME
        );
        eventCoupon.validateCoupon();
        eventCoupon.deliverCoupon(100);

        // when
        assertThrows(IllegalArgumentException.class, eventCoupon::validateCoupon);
    }

    @Test
    @DisplayName("쿠폰이벤트 수량을 넘으면 넘은만큼 반환되고 예외도 검증하는 테스트")
    void shouldReturnExcessCouponsAndValidateException() {
        // given
        EventCoupon eventCoupon = EventCoupon.createEventCoupon(
                "testEvent",
                10,
                CouponEventType.FIRST_COME
        );
        int deliveryCoupon = 15;

        // when
        eventCoupon.validateCoupon();
        eventCoupon.deliverCoupon(deliveryCoupon);
        assertThrows(IllegalArgumentException.class, eventCoupon::validateCoupon);

        // then
        assertEquals(eventCoupon.getIssuedCount(), eventCoupon.getUsedCount());
    }

    @Test
    @DisplayName("이밴트가 종료되면_쿠폰이 만료되어야 한다.")
    void shouldCouponExpiredWhenEventEnd() {
        // given
        String userId = "testUser";
        EventCoupon eventCoupon = EventCoupon.createEventCoupon(
                "testEvent",
                5,
                CouponEventType.FIRST_COME
        );
        FirstComeCouponStrategy firstComeCouponStrategy = new FirstComeCouponStrategy(
                LocalDateTime.now().plusDays(1),
                eventCoupon
        );

        for (Coupon object : List.of(
                firstComeCouponStrategy.apply(userId),
                firstComeCouponStrategy.apply(userId))) {
            eventCoupon.addCoupon(object);
        }
        // when
        eventCoupon.endEvent();

        // then
        eventCoupon.getCoupons().forEach(coupon -> {
            assertTrue(coupon.isExpired());
        });
    }
}