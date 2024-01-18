package com.paymentic.domain.events.data;


import com.paymentic.domain.shared.BuyerInfo;
import com.paymentic.domain.shared.CardInfo;

public record RefundData(String id,String idempotenceKey,
                         BuyerInfo buyerInfo, CardInfo cardInfo) {
}
