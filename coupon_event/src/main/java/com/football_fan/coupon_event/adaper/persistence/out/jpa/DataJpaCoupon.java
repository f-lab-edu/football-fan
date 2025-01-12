package com.football_fan.coupon_event.adaper.persistence.out.jpa;

import com.football_fan.coupon_event.adaper.persistence.out.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataJpaCoupon extends JpaRepository<Coupon, Long> {

}
