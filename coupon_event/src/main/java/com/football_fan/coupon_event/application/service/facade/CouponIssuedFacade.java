package com.football_fan.coupon_event.application.service.facade;

import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;
import com.football_fan.coupon_event.application.port.in.usecase.CouponUseCase;
import com.football_fan.coupon_event.application.service.coupon.CouponIssueService;
import com.football_fan.coupon_event.application.service.coupon.CouponRedisService;
import com.football_fan.coupon_event.application.service.exception.RedisLockAcquisitionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class CouponIssuedFacade implements CouponUseCase {
    // 쿠폰을 발급해주는 서비스
    private final CouponRedisService couponRedisService;
    private final CouponIssueService couponIssueService;

    @Override
    public int issueCoupon(Long eventId, String userId) {
        try {
            // 쿠폰 이벤트 정보 확인
            EventCoupon eventInformation = couponIssueService.getEventInformation(eventId);
            // 쿠폰 발급 처리
            return couponRedisService.addParticipant(eventId, userId, eventInformation.getIssuedCount());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Event not found");
        }
    }

    @Scheduled(cron = "0/10 * * * * *")
    public void proceedCouponIssued() {
        log.debug("Cron job started: proceedCouponIssued");
        // 쿠폰 이벤트 키를 찾기.
        // 쿠폰 이벤트 키가 없으면 리턴
        // 어떤 이벤트 이던간 발급해야함. 키를 다 가져오자.
        extractEventIdFromKey().ifPresentOrElse(
                this::proceedCouponIssuedForEvent, () -> log.debug("No matching queue found."));
    }

    private void updateEventStatus(Long eventId, List<String> participants) {
        // 이벤트 참여가 가능한 유저만 필터링
        try {
            couponIssueService.updateEventCoupon(eventId, participants.size());
        } catch (RedisLockAcquisitionException e) {
            log.error("Redis lock acquisition failed");
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    private void proceedCouponIssuedForEvent(String eventId) {
        // 레디스에서 쿠폰이벤트 참여 정보를 가져온다.
        EventCoupon eventCoupon = couponIssueService.getEventInformation(Long.parseLong(eventId));

        // 참여한 유저수가 쿠폰 맥시멈 미만이면 쿠폰 발급 프로세스 진행
        if (eventCoupon.isFull()) {
            return;
        }
        List<String> participants = couponRedisService.getParticipants(eventCoupon);
        // 이벤트 참여가 가능한 유저만 필터링
        updateEventStatus(Long.parseLong(eventId), participants);
        // 참여한 유저에게 쿠폰 발급
        couponIssueService.createAndSaveCoupons(participants.subList(0, eventCoupon.getIssuedCount()), eventCoupon);
        // 쿠폰 발급 프로세스 진행 후 레디스에서 해당 이벤트 정보 삭제
        couponRedisService.deleteEvent(eventId);
    }

//    private static List<String> getValidParticipants(Optional<Integer> optionalExceedCount, List<String> participants) {
//        int exceedCount = optionalExceedCount.get();
//        if (exceedCount > 0) {
//            int validatedCount = participants.size() - exceedCount;
//            participants = participants.subList(0, validatedCount);
//        }
//        return participants;
//    }

    private Optional<String> extractEventIdFromKey() {
        return Optional.of(couponRedisService.getKeySetsForEvent(CouponRedisService.EVENT_KEY + "*"));
    }
}
