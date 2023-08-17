package com.paymentic.domain.events;

import com.paymentic.domain.Checkout;

public record PaymentCreatedEvent(Checkout event) {
  private static final String EVENT_TYPE = "paymentic.payments.gateway.v1.payment.created";
  private static final String SUBJECT = "new-payment-request";
  private static final String SOURCE_PATTERN = "/payments/%s";
  public String type() {
    return EVENT_TYPE;
  }
  public String source() {
    return String.format(SOURCE_PATTERN,event.getId().toString());
  }
  public String subject() {
    return SUBJECT;
  }

}
