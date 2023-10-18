package com.paymentic.domain.specification;

import com.paymentic.domain.PaymentOrder;
import com.paymentic.domain.PaymentOrderStatus;

public class PaymentOrderClosed implements Specification<PaymentOrder> {
  @Override
  public boolean IsSatisfiedBy(PaymentOrder element) {
    return element.getWalletUpdated() && element.getLedgerUpdated()
        && PaymentOrderStatus.SUCCESS.equals(element.getStatus());
  }
}
