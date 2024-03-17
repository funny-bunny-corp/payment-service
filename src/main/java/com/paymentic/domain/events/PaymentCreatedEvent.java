package com.paymentic.domain.events;

import com.paymentic.domain.events.data.CheckoutData;
import com.paymentic.domain.events.data.PaymentOrderData;
import java.util.List;

public record PaymentCreatedEvent(CheckoutData checkout, PaymentOrderData payment) {
  private static final String EVENT_TYPE = "funny-bunny.xyz.payment-processing.v1.payment.created";
  private static final String SUBJECT = "/payments/%s";
  private static final String SOURCE_PATTERN = "/checkout/%s";
  public String type() {
    return EVENT_TYPE;
  }
  public String source() {
    return String.format(SOURCE_PATTERN,checkout.id().toString());
  }
  public String subject() {
    return String.format(SUBJECT, payment.id().toString());
  }

}
