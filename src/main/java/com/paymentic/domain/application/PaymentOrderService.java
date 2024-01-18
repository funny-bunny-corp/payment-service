package com.paymentic.domain.application;

import com.paymentic.domain.CheckoutId;
import com.paymentic.domain.PaymentOrder;
import com.paymentic.domain.repositories.PaymentOrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderService {
  private final PaymentOrderRepository paymentOrderRepository;
  public PaymentOrderService(PaymentOrderRepository paymentOrderRepository) {
    this.paymentOrderRepository = paymentOrderRepository;
  }
  public List<PaymentOrder> ordersFromCheckout(String id){
    return this.paymentOrderRepository.ordersFromCheckout(new CheckoutId(id));
  }

}
