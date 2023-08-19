package com.paymentic.domain.application;

import com.paymentic.domain.Checkout;
import com.paymentic.domain.CheckoutId;
import com.paymentic.domain.PaymentOrder;
import com.paymentic.domain.events.PaymentCreatedEvent;
import com.paymentic.domain.events.PaymentOrderEvent;
import com.paymentic.domain.events.data.CheckoutData;
import com.paymentic.domain.events.data.PaymentOrderData;
import com.paymentic.domain.events.publisher.PaymentEventsPublisher;
import com.paymentic.domain.repositories.CheckoutRepository;
import com.paymentic.domain.shared.BuyerInfo;
import com.paymentic.domain.shared.CardInfo;
import com.paymentic.domain.shared.SellerInfo;
import java.util.stream.Collectors;
import org.openapitools.model.PaymentRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckoutService {
  private final CheckoutRepository checkoutRepository;
  private final ApplicationEventPublisher publisher;

  private final PaymentEventsPublisher eventsBridge;

  public CheckoutService(CheckoutRepository checkoutRepository, ApplicationEventPublisher publisher,
      PaymentEventsPublisher eventsBridge) {
    this.checkoutRepository = checkoutRepository;
    this.publisher = publisher;
    this.eventsBridge = eventsBridge;
  }
  @Transactional
  public Checkout process(PaymentRequest request){
    var checkout = Checkout.newCheckoutInitiated(new BuyerInfo(request.getBuyerInfo().getDocument(), request.getBuyerInfo().getName()),
        new CardInfo(request.getCreditCardInfo().getCardInfo(),
            request.getCreditCardInfo().getToken()));
    this.checkoutRepository.save(checkout);
    var payments = request.getPaymentOrders().stream().map(paymentOrder -> {
      var payment = PaymentOrder.newPaymentInitiated(paymentOrder.getAmount(),paymentOrder.getCurrency(),new CheckoutId(request.getCheckoutId()),new SellerInfo(paymentOrder.getSellerAccount()));
      this.publisher.publishEvent(new PaymentOrderEvent(this,new CheckoutId(request.getCheckoutId()),payment));
      return new PaymentOrderData(payment.getId(),payment.getAmount(),payment.getCurrency(),payment.getStatus());
    }).collect(Collectors.toList());
    var checkoutData = new CheckoutData(checkout.getId(),checkout.getBuyerInfo(),checkout.getCardInfo());
    this.eventsBridge.paymentCreated(new PaymentCreatedEvent(checkoutData,payments));
    return checkout;
  }

}
