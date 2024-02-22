package com.paymentic.domain.events.data;

import com.paymentic.domain.shared.BuyerInfo;
import com.paymentic.domain.shared.CardInfo;
import java.time.LocalDateTime;
import java.util.UUID;

public record CheckoutData(UUID id, BuyerInfo buyerInfo, CardInfo cardInfo, String idempotencyKey,
                           LocalDateTime at) { }
