package com.paymentic.domain;

public record RequestRefund(String amount,String currency,PaymentOrderId paymentOrder) {}
