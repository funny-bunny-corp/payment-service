package com.paymentic.domain.repositories;

import com.paymentic.domain.PaymentOrder;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PaymentOrderRepository extends CrudRepository<PaymentOrder, UUID> {

  default void markStarted(UUID id){
    var paymentOrder = this.findById(id);
    paymentOrder.ifPresent(po -> this.save(po.markStarted()));
  }

  default void walletUpdated(UUID id){
    var paymentOrder = this.findById(id);
    paymentOrder.ifPresent(po -> this.save(po.walletUpdated()));
  }

}
