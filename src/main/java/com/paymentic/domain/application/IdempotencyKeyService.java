package com.paymentic.domain.application;

import com.paymentic.domain.IdempotencyKey;
import com.paymentic.domain.events.IdempotencyKeyRequestUsage;
import com.paymentic.domain.exception.IdempotencyKeyAlreadyUsed;
import com.paymentic.domain.repositories.IdempotencyKeyRepository;
import java.util.UUID;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class IdempotencyKeyService {
  private final IdempotencyKeyRepository idempotencyKeyRepository;
  public IdempotencyKeyService(IdempotencyKeyRepository idempotencyKeyRepository) {
    this.idempotencyKeyRepository = idempotencyKeyRepository;
  }
  public void handleUsage(IdempotencyKeyRequestUsage event) {
    try {
      this.idempotencyKeyRepository.insert(new IdempotencyKey(UUID.fromString(event.getIdempotencyKey())));
    }catch (DataIntegrityViolationException e){
      throw new IdempotencyKeyAlreadyUsed();
    }
  }
}
