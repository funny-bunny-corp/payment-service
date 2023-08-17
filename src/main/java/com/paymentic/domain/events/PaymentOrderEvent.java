package com.paymentic.domain.events;

import com.paymentic.domain.CheckoutId;
import com.paymentic.domain.PaymentOrder;
import org.springframework.context.ApplicationEvent;

public class PaymentOrderEvent extends ApplicationEvent {
  private final CheckoutId checkoutId;
  private final PaymentOrder paymentOrder;
  public PaymentOrderEvent(Object source, CheckoutId checkoutId, PaymentOrder order) {
    super(source);
    this.checkoutId = checkoutId;
    this.paymentOrder = order;
  }
  public CheckoutId getCheckoutId() {
    return checkoutId;
  }
  public PaymentOrder getPaymentOrder() {
    return paymentOrder;
  }

}
