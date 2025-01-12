package com.football_fan.coupon_event.adaper.persistence.out.eventcoupon;

import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;
import com.football_fan.coupon_event.adaper.persistence.out.jpa.DataJpaEventCoupon;
import com.football_fan.coupon_event.application.port.out.persistence.rdb.FindEventCouponPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class EventCouponQuery implements FindEventCouponPort {
    private final DataJpaEventCoupon eventCouponRepository;

    @Override
    public Optional<EventCoupon> findEventById(Long id) {
        return eventCouponRepository.findById(id);
    }
}
