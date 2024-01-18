package com.paymentic.domain.repositories;

import com.paymentic.domain.Refund;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface RefundRepository extends CrudRepository<Refund, UUID> { }
