package com.paymentic.domain.application;

import com.paymentic.domain.Checkout;
import com.paymentic.domain.PaymentOrderId;
import com.paymentic.domain.Refund;
import com.paymentic.domain.RequestRefund;
import com.paymentic.domain.events.IdempotencyKeyRequestUsage;
import com.paymentic.domain.events.RefundCreated;
import com.paymentic.domain.events.RefundCreatedEvent;
import com.paymentic.domain.events.data.PaymentOrderData;
import com.paymentic.domain.events.data.RefundData;
import com.paymentic.domain.events.publisher.RefundEventsPublisher;
import com.paymentic.domain.exception.PaymentOrderNotFound;
import com.paymentic.domain.repositories.CheckoutRepository;
import com.paymentic.domain.repositories.PaymentOrderRepository;
import com.paymentic.domain.repositories.RefundRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RefundService {
  private final RefundRepository refundRepository;
  private final PaymentOrderRepository paymentOrderRepository;
  private final RefundEventsPublisher refundEventsPublisher;
  private final IdempotencyKeyService idempotencyKeyService;
  private final CheckoutRepository checkoutRepository;
  public RefundService(RefundRepository refundRepository,
      PaymentOrderRepository paymentOrderRepository, RefundEventsPublisher refundEventsPublisher,
      IdempotencyKeyService idempotencyKeyService, CheckoutRepository checkoutRepository) {
    this.refundRepository = refundRepository;
    this.paymentOrderRepository = paymentOrderRepository;
    this.refundEventsPublisher = refundEventsPublisher;
    this.idempotencyKeyService = idempotencyKeyService;
    this.checkoutRepository = checkoutRepository;
  }
  public Refund requestRefund(RequestRefund requestRefund,String idempotencyKey){
    IdempotencyKeyRequestUsage idempotencyKeyRequestUsage = new IdempotencyKeyRequestUsage(this, idempotencyKey);
    idempotencyKeyService.handleUsage(idempotencyKeyRequestUsage);
    var optionalPaymentOrder = this.paymentOrderRepository.findById(
        UUID.fromString(requestRefund.paymentOrder().getId()));
    if (optionalPaymentOrder.isPresent()){
      var paymentOrder = optionalPaymentOrder.get();
      var refund = new Refund(new PaymentOrderId(paymentOrder.getId().toString()), LocalDateTime.now(),requestRefund.amount(),requestRefund.currency());
      this.refundRepository.save(refund);
      Optional<Checkout> optionalCheckout = this.checkoutRepository.findById(
          UUID.fromString(paymentOrder.getCheckout().getId()));
      optionalCheckout.ifPresent(checkout -> {
        var payment = new PaymentOrderData(paymentOrder.getId(),paymentOrder.getAmount(),paymentOrder.getCurrency(),paymentOrder.getStatus(),
            paymentOrder.getSellerInfo(),paymentOrder.getIdempotencyKey());
        var refundData = new RefundData(refund.getId().toString(),idempotencyKey,checkout.getBuyerInfo(),checkout.getCardInfo());
        this.refundEventsPublisher.refundCreated(new RefundCreatedEvent(new RefundCreated(refundData,payment)));
      });
      return refund;
    }else {
      throw new PaymentOrderNotFound();
    }
  }

}
