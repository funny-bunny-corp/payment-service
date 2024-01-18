package com.paymentic.domain.events.publisher;

import com.paymentic.domain.events.RefundCreatedEvent;

public interface RefundEventsPublisher {
  void refundCreated(RefundCreatedEvent event);

}
