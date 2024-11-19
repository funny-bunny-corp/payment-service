package com.paymentic.domain.application;


import com.paymentic.domain.events.CheckoutClosedEvent;
import com.paymentic.domain.events.PaymentDoneEvent;
import com.paymentic.domain.events.PaymentOrderBookedEvent;
import com.paymentic.domain.events.publisher.PaymentEventsPublisher;
import com.paymentic.domain.repositories.PaymentOrderRepository;
import com.paymentic.domain.specification.PaymentOrderClosed;
import java.time.LocalDateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class CloseCheckoutService implements ApplicationListener<PaymentOrderBookedEvent> {
  private final PaymentOrderRepository paymentOrderRepository;
  private final ApplicationEventPublisher publisher;
  private final PaymentEventsPublisher paymentEventsPublisher;
  public CloseCheckoutService(PaymentOrderRepository paymentOrderRepository,
      ApplicationEventPublisher publisher, PaymentEventsPublisher paymentEventsPublisher) {
    this.paymentOrderRepository = paymentOrderRepository;
    this.publisher = publisher;
    this.paymentEventsPublisher = paymentEventsPublisher;
  }
  @Override
  public void onApplicationEvent(PaymentOrderBookedEvent event) {
    var orders = this.paymentOrderRepository.ordersFromCheckout(event.getCheckoutId());
    var data = orders.stream().filter(order -> new PaymentOrderClosed().IsSatisfiedBy(order))
        .findAny();
    if (data.isEmpty()) {
      this.publisher.publishEvent(new CheckoutClosedEvent(this, event.getCheckoutId()));
      this.paymentEventsPublisher.paymentDone(new PaymentDoneEvent(event.getCheckoutId(),
          LocalDateTime.now()));
    }
  }
}
