package com.paymentic.domain.application;

import com.paymentic.domain.Checkout;
import com.paymentic.domain.CheckoutId;
import com.paymentic.domain.PaymentOrder;
import com.paymentic.domain.events.PaymentCreatedEvent;
import com.paymentic.domain.events.PaymentOrderEvent;
import com.paymentic.domain.events.publisher.PaymentEventsPublisher;
import com.paymentic.domain.repositories.CheckoutRepository;
import com.paymentic.domain.shared.BuyerInfo;
import com.paymentic.domain.shared.CardInfo;
import com.paymentic.domain.shared.SellerInfo;
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
    request.getPaymentOrders().forEach(paymentOrder -> {
      var payment = PaymentOrder.newPaymentInitiated(paymentOrder.getAmount(),paymentOrder.getCurrency(),new CheckoutId(request.getCheckoutId()),new SellerInfo(paymentOrder.getSellerAccount()));
      this.publisher.publishEvent(new PaymentOrderEvent(this,new CheckoutId(request.getCheckoutId()),payment));
    });
    this.eventsBridge.paymentCreated(new PaymentCreatedEvent(checkout));
    return checkout;
  }

}
