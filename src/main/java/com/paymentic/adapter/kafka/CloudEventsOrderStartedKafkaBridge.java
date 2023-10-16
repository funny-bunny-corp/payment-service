package com.paymentic.adapter.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentic.domain.events.PaymentOrderStartedEvent;
import com.paymentic.domain.repositories.PaymentOrderRepository;
import com.paymentic.infra.events.Event;
import com.paymentic.infra.events.service.EventService;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.CloudEventUtils;
import io.cloudevents.core.data.PojoCloudEventData;
import io.cloudevents.jackson.PojoCloudEventDataMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CloudEventsOrderStartedKafkaBridge {
  private static final String PAYMENT_ORDER_STARTED_EVENT_TYPE = "paymentic.payments.gateway.v1.payment.order.started";
  private static final Logger LOGGER = LoggerFactory.getLogger(CloudEventsOrderStartedKafkaBridge.class);
  private static final String ERROR = "Event %s already handled!!!";
  private final ObjectMapper mapper;
  private final PaymentOrderRepository paymentOrderRepository;
  private final EventService eventService;

  public CloudEventsOrderStartedKafkaBridge(ObjectMapper mapper,
      PaymentOrderRepository paymentOrderRepository, EventService eventService) {
    this.mapper = mapper;
    this.paymentOrderRepository = paymentOrderRepository;
    this.eventService = eventService;
  }
  @KafkaListener(id = "paymentOrderStarted",groupId = "payment-service-group-id", topics = "payments")
  public void listen(CloudEvent message){
    var handle = this.eventService.shouldHandle(new Event(UUID.fromString(message.getId())));
    if (handle){
      if (PAYMENT_ORDER_STARTED_EVENT_TYPE.equals(message.getType())){
        PojoCloudEventData<PaymentOrderStartedEvent> deserializedData = CloudEventUtils
            .mapData(message, PojoCloudEventDataMapper.from(mapper, PaymentOrderStartedEvent.class));
        var paymentOrderStarted = deserializedData.getValue();
        this.paymentOrderRepository.markStarted(UUID.fromString(paymentOrderStarted.id()));
      }
    }else {
      LOGGER.error(String.format(ERROR, message.getId()));
    }
  }

}
