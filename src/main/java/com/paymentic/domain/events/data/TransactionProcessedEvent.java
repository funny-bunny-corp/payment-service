package com.paymentic.domain.events.data;

import com.paymentic.domain.shared.BuyerInfo;
import java.time.LocalDateTime;

public record TransactionProcessedEvent(PaymentOrderId payment, String amount, String currency,
                                        LocalDateTime at, BuyerInfo buyer) {}
