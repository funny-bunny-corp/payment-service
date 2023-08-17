package com.paymentic.domain.application;

import com.paymentic.domain.events.PaymentOrderEvent;
import com.paymentic.domain.repositories.PaymentOrderRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderCreationService implements ApplicationListener<PaymentOrderEvent> {
  private final PaymentOrderRepository paymentOrderRepository;

  public PaymentOrderCreationService(PaymentOrderRepository paymentOrderRepository) {
    this.paymentOrderRepository = paymentOrderRepository;
  }
  @Override
  public void onApplicationEvent(PaymentOrderEvent event) {
    this.paymentOrderRepository.save(event.getPaymentOrder());
  }

}
