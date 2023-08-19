package com.paymentic.domain.events;

import com.paymentic.domain.Checkout;
import com.paymentic.domain.PaymentOrder;
import com.paymentic.domain.events.data.CheckoutData;
import com.paymentic.domain.events.data.PaymentOrderData;
import java.util.List;

public record PaymentCreatedEvent(CheckoutData checkout, List<PaymentOrderData> payments) {
  private static final String EVENT_TYPE = "paymentic.payments.gateway.v1.payment.created";
  private static final String SUBJECT = "new-payment-request";
  private static final String SOURCE_PATTERN = "/payments/%s";
  public String type() {
    return EVENT_TYPE;
  }
  public String source() {
    return String.format(SOURCE_PATTERN,checkout.id().toString());
  }
  public String subject() {
    return SUBJECT;
  }

}
