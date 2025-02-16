package com.football_fan.payments.adapter.in.web.view;

import com.football_fan.payments.common.WebAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@WebAdapter
@RequestMapping("/payment")
public class CheckoutController {

    @GetMapping("")
    public String checkoutPage() {
        return "checkout";
    }
}
