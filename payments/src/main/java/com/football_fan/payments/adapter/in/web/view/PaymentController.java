package com.football_fan.payments.adapter.in.web.view;

import com.football_fan.payments.common.WebAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@WebAdapter
@Controller
@RequestMapping
public class PaymentController {

    @GetMapping("/success")
    public Mono<String> successPage() {
        return Mono.just("success");
    }

    @GetMapping("/fail")
    public Mono<String> failPage() {
        return Mono.just("fail");
    }
}
