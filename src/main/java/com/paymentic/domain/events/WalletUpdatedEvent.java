package com.paymentic.domain.events;

import com.paymentic.domain.events.data.PaymentOrderId;

public record WalletUpdatedEvent(PaymentOrderId payment) {}
