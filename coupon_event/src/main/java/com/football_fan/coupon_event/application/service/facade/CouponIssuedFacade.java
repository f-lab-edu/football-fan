package com.football_fan.coupon_event.application.service.facade;

import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;
import com.football_fan.coupon_event.application.port.in.usecase.CouponUseCase;
import com.football_fan.coupon_event.application.service.coupon.CouponIssueService;
import com.football_fan.coupon_event.application.service.coupon.CouponRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CouponIssuedFacade implements CouponUseCase {
    // 쿠폰을 발급해주는 서비스
    private final CouponRedisService couponRedisService;
    private final CouponIssueService couponIssueService;

    @Override
    public int issueCoupon(String eventId, String userId) {
        // 쿠폰 이벤트 정보 확인
        EventCoupon eventInformation = couponIssueService.getEventInformation(Long.parseLong(eventId));
        // 쿠폰 발급 처리
        return couponRedisService.addParticipant(eventId, userId, eventInformation.getIssuedCount());
    }

    @Scheduled(cron = "0/10 * * * * *")
    public void proceedCouponIssued() {
        log.debug("Cron job started: proceedCouponIssued");
        // 쿠폰 발급 처리
//        couponRedisService.proceedCouponIssued();
    }

}
