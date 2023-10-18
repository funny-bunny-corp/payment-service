package com.paymentic.domain.exception;

public class PaymentOrderNotFound extends RuntimeException{
  @Override
  public String getMessage() {
    return "Payment Order Id no found";
  }
}
