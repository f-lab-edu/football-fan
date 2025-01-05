package com.football_fan.coupon_event.application.port.out.persistence.inmeomry;

public interface InMemoryFindOutputPort {
    <T> T find(String key, Class<T> type);
}
