package com.paymentic.infra.events.repository;

import com.paymentic.infra.events.Event;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EventRepository extends CrudRepository<Event, UUID> {

  @Modifying
  @Transactional
  @Query(value = "INSERT INTO payment_service_event VALUES ( :#{#event.id} )", nativeQuery = true)
  void insert(@Param("event") Event event);

}
