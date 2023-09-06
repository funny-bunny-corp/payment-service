package com.paymentic.adapter.http;

import com.paymentic.domain.application.CheckoutService;
import org.openapitools.api.V1Api;
import org.openapitools.model.PaymentCreated;
import org.openapitools.model.PaymentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutResource implements V1Api {
  private final CheckoutService checkoutService;
  public CheckoutResource(CheckoutService checkoutService) {
    this.checkoutService = checkoutService;
  }
  @Override
  public ResponseEntity<PaymentCreated> createPayment(String idempotencyKey, PaymentRequest paymentRequest) {
    var paymentCreated = this.checkoutService.process(paymentRequest,idempotencyKey);
    return new ResponseEntity<>(new PaymentCreated().id(paymentCreated.getId().toString()), HttpStatus.CREATED);
  }
}
