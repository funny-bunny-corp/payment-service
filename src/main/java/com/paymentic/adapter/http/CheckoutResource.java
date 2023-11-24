package com.paymentic.adapter.http;

import com.paymentic.domain.Checkout;
import com.paymentic.domain.PaymentOrderStatus;
import com.paymentic.domain.application.CheckoutService;
import com.paymentic.domain.application.PaymentOrderService;
import java.util.List;
import java.util.stream.Collectors;
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
  private final PaymentOrderService paymentOrderService;
  public CheckoutResource(CheckoutService checkoutService, PaymentOrderService paymentOrderService) {
    this.checkoutService = checkoutService;
    this.paymentOrderService = paymentOrderService;
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
    var orders = this.paymentOrderService.ordersFromCheckout(id).stream().map( o ->new PaymentOrder().id(o.getId().toString()).status(convert(o.getStatus()))).collect(
        Collectors.toList());
    return ResponseEntity.ok(orders);
  }
  private PaymentOrder.StatusEnum convert(PaymentOrderStatus status){
    return PaymentOrder.StatusEnum.valueOf(status.name());
  }

}
