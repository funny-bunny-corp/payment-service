package com.paymentic.domain.exception;

public class IdempotencyKeyAlreadyUsed extends RuntimeException{
  @Override
  public String getMessage() {
    return "Checkout request already sent";
  }
}
