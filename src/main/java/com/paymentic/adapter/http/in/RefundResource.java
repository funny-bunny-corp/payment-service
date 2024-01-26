package com.paymentic.adapter.http.in;

import com.paymentic.domain.PaymentOrderId;
import com.paymentic.domain.RequestRefund;
import com.paymentic.domain.application.RefundService;
import org.openapitools.api.RefundsApi;
import org.openapitools.model.PaymentCreated;
import org.openapitools.model.RefundRequest;
import org.openapitools.model.RefundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefundResource implements RefundsApi {
  private final RefundService refundService;
  public RefundResource(RefundService refundService) {
    this.refundService = refundService;
  }
  @Override
  public ResponseEntity<RefundResponse> createRefund(String idempotencyKey,RefundRequest refundRequest) {
    var request = new RequestRefund(refundRequest.getAmount(),refundRequest.getCurrency(), new PaymentOrderId(refundRequest.getPaymentId()));
    var refund = this.refundService.requestRefund(request,idempotencyKey);
    var response =  new RefundResponse().refundId(refund.getId().toString()).status("received");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

}
