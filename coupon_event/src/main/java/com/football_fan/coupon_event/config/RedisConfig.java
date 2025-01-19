package com.football_fan.coupon_event.config;

import com.football_fan.coupon_event.adaper.persistence.out.redis.CouponRepository;
import com.football_fan.coupon_event.application.port.out.persistence.inmeomry.InMemoryFindOutputPort;
import com.football_fan.coupon_event.application.port.out.persistence.inmeomry.InMemorySaveOutputPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Key는 문자열, Value는 JSON으로 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Hash의 Key와 Value도 동일하게 설정
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CouponRepository couponRepository(RedisTemplate<String, String> redisTemplate) {
        return new CouponRepository(redisTemplate);
    }

    @Bean
    public InMemorySaveOutputPort inMemorySaveOutputPort(CouponRepository couponRepository) {
        return couponRepository;
    }

    @Bean
    public InMemoryFindOutputPort inMemoryFindOutputPort(CouponRepository couponRepository) {
        return couponRepository;
    }

}



