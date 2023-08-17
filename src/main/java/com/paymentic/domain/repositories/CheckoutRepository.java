package com.paymentic.domain.repositories;

import com.paymentic.domain.Checkout;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface CheckoutRepository extends CrudRepository<Checkout, UUID> { }
