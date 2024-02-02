package com.paymentic.adapter.kafka.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentic.domain.events.data.TransactionProcessedEvent;
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
public class CloudEventsOrderDeclinedKafkaBridge {
  private static final String PAYMENT_ORDER_DECLINED_EVENT_TYPE = "paymentic.io.payment-processing.v1.payment-order.declined";
  private static final Logger LOGGER = LoggerFactory.getLogger(
      CloudEventsOrderDeclinedKafkaBridge.class);
  private static final String ERROR = "Event %s already handled!!!";
  private final ObjectMapper mapper;
  private final PaymentOrderRepository paymentOrderRepository;
  private final EventService eventService;
  public CloudEventsOrderDeclinedKafkaBridge(ObjectMapper mapper,
      PaymentOrderRepository paymentOrderRepository, EventService eventService) {
    this.mapper = mapper;
    this.paymentOrderRepository = paymentOrderRepository;
    this.eventService = eventService;
  }
  @KafkaListener(id = "paymentOrderDeclined", topics = "${consumers.payment-processing-topic}")
  public void listen(CloudEvent message) {
    if (PAYMENT_ORDER_DECLINED_EVENT_TYPE.equals(message.getType())) {
      var handle = this.eventService.shouldHandle(new Event(UUID.fromString(message.getId())));
      if (handle) {
        PojoCloudEventData<TransactionProcessedEvent> deserializedData = CloudEventUtils
            .mapData(message,PojoCloudEventDataMapper.from(mapper, TransactionProcessedEvent.class));
        var order = deserializedData.getValue();
        this.paymentOrderRepository.markFailed(UUID.fromString(order.payment().getId()));
      }else {
        LOGGER.error(String.format(ERROR, message.getId()));
      }
    }
  }

}
