package com.paymentic.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity(name = "idempotency_key")
public class IdempotencyKey{
  @Id
  @Column(name = "key")
  private UUID id;
  public IdempotencyKey(){}
  public IdempotencyKey(UUID id) {
    this.id = id;
  }
  public UUID getId() {
    return id;
  }
}
