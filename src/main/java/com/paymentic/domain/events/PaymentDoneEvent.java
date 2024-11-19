package com.paymentic.domain.events;

import com.paymentic.domain.CheckoutId;
import com.paymentic.domain.events.data.CheckoutData;
import com.paymentic.domain.events.data.PaymentOrderData;
import java.time.LocalDateTime;
import java.util.List;

public record PaymentDoneEvent(CheckoutId checkout, LocalDateTime at) {
  private static final String EVENT_TYPE = "funny-bunny.xyz.payment-processing.v1.payment.done";
  private static final String SUBJECT = "payment-done";
  private static final String SOURCE_PATTERN = "/payments/%s";
  public String type() {
    return EVENT_TYPE;
  }
  public String source() {
    return String.format(SOURCE_PATTERN, checkout.getId());
  }
  public String subject() {
    return SUBJECT;
  }

}
