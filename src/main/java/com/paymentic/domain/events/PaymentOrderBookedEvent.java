package com.paymentic.domain.events;

import com.paymentic.domain.CheckoutId;
import com.paymentic.domain.events.data.PaymentOrderId;
import org.springframework.context.ApplicationEvent;

public class PaymentOrderBookedEvent extends ApplicationEvent {
  private final CheckoutId checkoutId;
  private final PaymentOrderId paymentOrderId;
  public PaymentOrderBookedEvent(Object source, CheckoutId checkoutId,
      PaymentOrderId paymentOrderId) {
    super(source);
    this.checkoutId = checkoutId;
    this.paymentOrderId = paymentOrderId;
  }
  public CheckoutId getCheckoutId() {
    return checkoutId;
  }

  public PaymentOrderId getPaymentOrderId() {
    return paymentOrderId;
  }
}
