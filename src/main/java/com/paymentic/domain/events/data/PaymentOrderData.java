package com.paymentic.domain.events.data;

import com.paymentic.domain.PaymentOrderStatus;
import com.paymentic.domain.shared.SellerInfo;
import java.util.UUID;

public record PaymentOrderData(UUID id, String amount, String currency, PaymentOrderStatus status,
                               SellerInfo sellerInfo) { }
