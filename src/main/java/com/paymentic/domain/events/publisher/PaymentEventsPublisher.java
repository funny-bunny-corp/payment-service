package com.paymentic.domain.events.publisher;

import com.paymentic.domain.events.PaymentCreatedEvent;

public interface PaymentEventsPublisher {
  void paymentCreated(PaymentCreatedEvent event);

}
