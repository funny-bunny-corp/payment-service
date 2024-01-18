package com.paymentic.domain.events;

import com.paymentic.domain.events.data.PaymentOrderData;
import com.paymentic.domain.events.data.RefundData;

public record RefundCreated(RefundData refund, PaymentOrderData payment) {}
