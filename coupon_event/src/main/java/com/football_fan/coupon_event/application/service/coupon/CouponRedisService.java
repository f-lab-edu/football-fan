package com.football_fan.coupon_event.application.service.coupon;

import com.football_fan.coupon_event.application.service.exception.DuplicateParticipationException;
import com.football_fan.coupon_event.application.service.exception.FullParticipantsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBatch;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionStage;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponRedisService {
    private final RedissonClient client;

    /**
     * 쿠폰 발급에 대한 이벤트를 발생시키는 서비스
     *
     * @param eventId 참가하기 위한 레디스 이벤트 식별자
     * @param userId  참가자 Id
     */
    public int addParticipant(String eventId, String userId, int maxParticipants) {
        String EVENT_KEY = "EVENT:SET";
        String eventGetKey = EVENT_KEY + eventId;
        RBatch batch = client.createBatch();

        // 명령 집합
        RFuture<Boolean> isMemberFuture = batch.getSet(eventGetKey).containsAsync(userId); // 중복 체크
        RFuture<Integer> sizeFuture = batch.getSet(eventGetKey).sizeAsync();              // 현재 크기 확인
        RFuture<Boolean> addFuture = batch.getSet(eventGetKey).addAsync(userId);          // 멤버 추가

        try {
            // 배치 실행
            batch.execute();
            // 명령 비동기 결과
            CompletionStage<Integer> commandStage = isMemberFuture
                    .thenCompose(isMember -> {
                        // 중복된 참가자라면 예외 발생
                        if (isMember) {
                            throw new DuplicateParticipationException("이미 참가 신청이 됐습니다!");
                        }
                        return sizeFuture.thenCompose(size -> {
                            if (size >= maxParticipants) {
                                throw new FullParticipantsException("참가자가 꽉 찼습니다.");
                            }
                            // 멤버 추가 진행
                            return addFuture.thenApply(isAdded -> {
                                if (!isAdded) {
                                    throw new RuntimeException("참가 신청 실패: 알 수 없는 이유.");
                                }
                                // 성공적으로 추가되었으므로 순번 반환
                                return size + 1;
                            });
                        });
                    });

            // 집합 동기 처리 결과 반환
            return commandStage.toCompletableFuture().get();
        } catch (Exception e) {
            log.error("참가 신청 처리 중 오류 발생: {} {}", eventGetKey, userId, e);
            throw new RuntimeException("참가 신청 처리 중 오류 발생", e);
        } finally {
            client.shutdown();
        }
    }

}
