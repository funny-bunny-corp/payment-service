package com.paymentic.domain.events;

import org.apache.kafka.common.protocol.types.Field.Str;
import org.springframework.context.ApplicationEvent;

public class IdempotencyKeyRequestUsage extends ApplicationEvent {
  private final String idempotencyKey;
  public IdempotencyKeyRequestUsage(Object source,String idempotencyKey) {
    super(source);
    this.idempotencyKey = idempotencyKey;
  }
  public String getIdempotencyKey() {
    return idempotencyKey;
  }
}
