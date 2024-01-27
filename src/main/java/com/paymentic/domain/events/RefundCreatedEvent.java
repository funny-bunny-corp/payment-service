package com.paymentic.domain.events;

import com.paymentic.domain.events.data.CheckoutData;
import com.paymentic.domain.events.data.PaymentOrderData;
import java.util.List;

public record RefundCreatedEvent(RefundCreated refund) {
  private static final String EVENT_TYPE = "paymentic.io.payment-processing.v1.refund.created";
  private static final String SUBJECT = "new-refund-request";
  private static final String SOURCE_PATTERN = "/refund/%s";
  public String type() {
    return EVENT_TYPE;
  }
  public String source() {
    return String.format(SOURCE_PATTERN, refund.refund().id());
  }
  public String subject() {
    return SUBJECT;
  }

}
