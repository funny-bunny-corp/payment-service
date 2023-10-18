package com.paymentic.domain.events;

import com.paymentic.domain.CheckoutId;
import org.springframework.context.ApplicationEvent;

public class CheckoutClosedEvent extends ApplicationEvent {
  private final CheckoutId checkoutId;
  public CheckoutClosedEvent(Object source, CheckoutId checkoutId) {
    super(source);
    this.checkoutId = checkoutId;
  }
  public CheckoutId getCheckoutId() {
    return checkoutId;
  }

}
