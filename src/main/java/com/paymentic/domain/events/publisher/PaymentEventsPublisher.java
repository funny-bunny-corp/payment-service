package com.paymentic.domain.events.publisher;

import com.paymentic.domain.events.PaymentCreatedEvent;
import com.paymentic.domain.events.PaymentDoneEvent;

public interface PaymentEventsPublisher {
  void paymentCreated(PaymentCreatedEvent event);
  void paymentDone(PaymentDoneEvent event);

}
