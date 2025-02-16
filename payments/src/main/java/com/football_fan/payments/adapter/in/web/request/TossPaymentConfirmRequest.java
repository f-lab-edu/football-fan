package com.football_fan.payments.adapter.in.web.request;

import lombok.Data;

@Data
public class TossPaymentConfirmRequest {
    String paymentKey;
    String orderId;
    Long amount;
}
