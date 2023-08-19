package com.paymentic.domain.events.data;

import com.paymentic.domain.PaymentOrderStatus;
import java.util.UUID;

public record PaymentOrderData(UUID id, String amount, String currency, PaymentOrderStatus status) { }
