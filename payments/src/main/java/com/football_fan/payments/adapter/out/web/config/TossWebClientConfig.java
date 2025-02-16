package com.football_fan.payments.adapter.out.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.Base64;

@Configuration
public class TossWebClientConfig {
    @Value("${psp.toss.url}")
    private String tossApiUrl;

    @Value("${psp.toss.secretKey}")
    private String tossApiKey;

    @Bean
    public WebClient tossWebClient() {
        String encode = Base64.getEncoder().encodeToString(tossApiKey.getBytes());

        return WebClient.builder()
                .baseUrl(tossApiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic %s".formatted(encode))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .clientConnector(reactorClientHttpConnector())
                .codecs(ClientCodecConfigurer::defaultCodecs)
                .build();
    }

    private ReactorClientHttpConnector reactorClientHttpConnector() {
        ConnectionProvider provider = ConnectionProvider.builder("toss-payment").build();
        return new ReactorClientHttpConnector(HttpClient.create(provider));
    }
}
