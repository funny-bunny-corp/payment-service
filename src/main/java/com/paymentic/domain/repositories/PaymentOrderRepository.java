package com.paymentic.domain.repositories;

import com.paymentic.domain.PaymentOrder;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PaymentOrderRepository extends CrudRepository<PaymentOrder, UUID> { }
