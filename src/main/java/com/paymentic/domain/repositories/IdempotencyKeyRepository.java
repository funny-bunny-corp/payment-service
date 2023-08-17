package com.paymentic.domain.repositories;

import com.paymentic.domain.IdempotencyKey;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface IdempotencyKeyRepository extends CrudRepository<IdempotencyKey, UUID> { }
