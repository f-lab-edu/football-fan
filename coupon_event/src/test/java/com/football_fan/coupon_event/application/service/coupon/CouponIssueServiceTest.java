package com.football_fan.coupon_event.application.service.coupon;

import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;
import com.football_fan.coupon_event.application.domain.CouponEventType;
import com.football_fan.coupon_event.application.port.out.persistence.rdb.FindEventCouponPort;
import com.football_fan.coupon_event.application.service.RedisService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


class CouponIssueServiceTest {

    @Mock
    private RedisService redisService;
    @Mock
    private FindEventCouponPort findEventCouponPort;
    @Mock
    private CouponIssueService couponIssueService;

    @Test
    void updateEventStatus_ShouldUpdateEventAndReturnExcessParticipants() {
        // Arrange
        Long eventId = 1L;
        int participationSize = 100;
        EventCoupon event = EventCoupon.createEventCoupon(
                "event",
                100,
                CouponEventType.FIRST_COME
        );

        when(redisService.acquireLock(anyString())).thenReturn(true);
        when(findEventCouponPort.findEventById(anyLong())).thenReturn(Optional.of(event));

        // Act
        couponIssueService.updateEventCoupon(eventId, participationSize);

    }


}