package com.football_fan.payments.adapter.out.web.config.toss.executor;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class TossPaymentExecutor {
    private WebClient tossPaymentWebClient;

    public Mono<String> execute(String paymentKey, String orderId, String amount) {
        String uri = "/v1/payments/confirm";
        return tossPaymentWebClient.post()
                .uri(uri)
                .bodyValue("""
                        {
                            "paymentKey": %s,
                            "orderId": %s,
                            "amount": %s
                        }
                        """.formatted(paymentKey, orderId, amount).trim())
                .retrieve()
                .bodyToMono(String.class);
    }
}
