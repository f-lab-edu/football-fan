package com.football_fan.payments.adapter.in.web.api;

import com.football_fan.payments.adapter.in.response.ApiResponse;
import com.football_fan.payments.adapter.in.web.request.TossPaymentConfirmRequest;
import com.football_fan.payments.adapter.out.web.config.toss.executor.TossPaymentExecutor;
import com.football_fan.payments.common.WebAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@WebAdapter
@RequestMapping("/v1/toss")
@RestController
public class TossPaymentsController {
    private TossPaymentExecutor tossPaymentExecutor;

    @RequestMapping("/confirm")
    Mono<ResponseEntity<ApiResponse<?>>> confirm(@RequestBody TossPaymentConfirmRequest request) {
        return tossPaymentExecutor.execute(
                request.getPaymentKey(), request.getOrderId(), String.valueOf(request.getAmount())
        ).map(it -> new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.CREATED, "confirmed", it),
                HttpStatus.CREATED
        ));
    }
}
