package com.football_fan.coupon_event.application.adaper.out.persistence.redis;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;
import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;
import com.football_fan.coupon_event.application.domain.CouponEventType;
import com.football_fan.coupon_event.application.domain.policy.FirstComeCouponStrategy;
import com.football_fan.coupon_event.application.port.out.persistence.inmeomry.InMemoryFindOutputPort;
import com.football_fan.coupon_event.application.port.out.persistence.inmeomry.InMemorySaveOutputPort;
import com.football_fan.coupon_event.config.RedisConfig;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = RedisConfig.class) // RedisConfig 클래스만 로드
class CouponRepositoryTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private InMemorySaveOutputPort inMemorySaveOutputPort;
    @Autowired
    private InMemoryFindOutputPort inMemoryFindOutputPort;

    @Test
    @DisplayName("동시에 여러 스레드가 쿠폰 이벤트를 증가시키는 테스트")
    void increaseAmountOfCouponCount() {
        // given
        int attendants = 1000;

        EventCoupon 선착순_이벤트 = EventCoupon.createEventCoupon("선착순 이벤트", 100, CouponEventType.FIRST_COME);
        Coupon firstComeCoupon = Coupon.createCoupon("1", new FirstComeCouponStrategy(
                LocalDateTime.now().plusHours(1),
                선착순_이벤트));

        // 쿠폰 만료 시간
        Duration duration = Duration.between(LocalDateTime.now(), firstComeCoupon.getExpirationDate());
        CountDownLatch countDownLatch = new CountDownLatch(attendants);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                attendants, attendants, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()
        );

        // when
        RedisTestContext testContext = new RedisTestContext();
        testContext.setRedisTemplate(redisTemplate);
        redisTemplate.delete(firstComeCoupon.getEventCoupon().getName());

        testContext.deleteAfterWorkingWithStrategyTemplate(firstComeCoupon, coupon -> {
            for (int i = 0; i < attendants; i++) {
                threadPoolExecutor.execute(() -> {
                    try {
                        inMemorySaveOutputPort.increaseAmountOfCouponCount(firstComeCoupon, 1, 1L, duration);
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }

            try {
                countDownLatch.await(); // 모든 작업 완료 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("스레드 대기 중 인터럽트 발생", e);
            }

            threadPoolExecutor.shutdown();
            // then
            Object object = inMemoryFindOutputPort.find(firstComeCoupon.getEventCoupon().getName(), Integer.class);
            assertEquals(attendants, object); // 예상 수량과 비교
        });
    }
}