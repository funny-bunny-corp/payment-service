package com.paymentic.adapter.http;

import com.paymentic.domain.Checkout;
import com.paymentic.domain.application.CheckoutService;
import java.util.List;
import org.openapitools.api.V1Api;
import org.openapitools.model.PaymentCreated;
import org.openapitools.model.PaymentData;
import org.openapitools.model.PaymentData.StatusEnum;
import org.openapitools.model.PaymentOrder;
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
    var paymentCreated = this.checkoutService.processPaymentRequest(paymentRequest,idempotencyKey);
    return new ResponseEntity<>(new PaymentCreated().id(paymentCreated.getId().toString()), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<PaymentData> getPayment(String id) {
    Checkout checkout = this.checkoutService.getById(id);
    return ResponseEntity.ok(new PaymentData().id(checkout.getId().toString()).status( Boolean.TRUE.equals(checkout.getPaymentDone())?
        StatusEnum.DONE : StatusEnum.IN_PROGRESS));
  }

  @Override
  public ResponseEntity<List<PaymentOrder>> getPaymentOrders(String id) {
    return V1Api.super.getPaymentOrders(id);
  }
}
