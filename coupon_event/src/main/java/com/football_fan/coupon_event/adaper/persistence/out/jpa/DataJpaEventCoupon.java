package com.football_fan.coupon_event.adaper.persistence.out.jpa;

import com.football_fan.coupon_event.adaper.persistence.out.entity.EventCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataJpaEventCoupon extends JpaRepository<EventCoupon, Long>{
    EventCoupon findEventById(Long id);

}
