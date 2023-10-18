package com.paymentic.domain.exception;

public class CheckoutAlreadyClosed extends RuntimeException{
  @Override
  public String getMessage() {
    return "Checkout already closed";
  }
}
