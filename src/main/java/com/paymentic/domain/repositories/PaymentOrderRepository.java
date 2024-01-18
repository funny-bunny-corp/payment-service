package com.paymentic.domain.repositories;

import com.paymentic.domain.CheckoutId;
import com.paymentic.domain.PaymentOrder;
import com.paymentic.domain.exception.PaymentOrderNotFound;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PaymentOrderRepository extends CrudRepository<PaymentOrder, UUID> {
  default void markStarted(UUID id){
    var paymentOrder = this.findById(id);
    paymentOrder.ifPresent(po -> this.save(po.markExecuting()));
  }

  default void walletUpdated(UUID id){
    var paymentOrder = this.findById(id);
    paymentOrder.ifPresent(po -> this.save(po.walletUpdated()));
  }
  default CheckoutId booked(UUID id){
    var paymentOrder = this.findById(id);
    if (paymentOrder.isPresent()){
      return this.save(paymentOrder.get().booked()).getCheckout();
    }
    throw new PaymentOrderNotFound();
  }

  default void markApproved(UUID id){
    var paymentOrder = this.findById(id);
    paymentOrder.ifPresent(po -> this.save(po.markApproved()));
  }

  default void markFailed(UUID id){
    var paymentOrder = this.findById(id);
    paymentOrder.ifPresent(po -> this.save(po.markFailed()));
  }
  default List<PaymentOrder> ordersFromCheckout(CheckoutId checkoutId){
    return findPaymentOrderByCheckout(checkoutId);
  }
  List<PaymentOrder> findPaymentOrderByCheckout(CheckoutId checkoutId);

}
