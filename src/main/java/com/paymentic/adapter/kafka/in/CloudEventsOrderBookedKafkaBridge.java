package com.paymentic.adapter.kafka.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentic.domain.events.PaymentOrderBookedEvent;
import com.paymentic.domain.events.data.TransactionBooked;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CloudEventsOrderBookedKafkaBridge {
  private static final String PAYMENT_ORDER_BOOKED_EVENT_TYPE = "funny-bunny.xyz.financial-reporting.v1.transaction.booked";
  private static final Logger LOGGER = LoggerFactory.getLogger(CloudEventsOrderBookedKafkaBridge.class);
  private static final String ERROR = "Event %s already handled!!!";
  private final ObjectMapper mapper;
  private final PaymentOrderRepository paymentOrderRepository;
  private final EventService eventService;
  private final ApplicationEventPublisher publisher;
  public CloudEventsOrderBookedKafkaBridge(ObjectMapper mapper,
      PaymentOrderRepository paymentOrderRepository, EventService eventService,
      ApplicationEventPublisher publisher) {
    this.mapper = mapper;
    this.paymentOrderRepository = paymentOrderRepository;
    this.eventService = eventService;
    this.publisher = publisher;
  }
  @KafkaListener(id = "paymentOrderBooked", topics = "${consumers.financial-reporting-topic}")
  public void listen(CloudEvent message) {
    if (PAYMENT_ORDER_BOOKED_EVENT_TYPE.equals(message.getType())) {
      var handle = this.eventService.shouldHandle(new Event(UUID.fromString(message.getId())));
      if (handle) {
        PojoCloudEventData<TransactionBooked> deserializedData = CloudEventUtils
            .mapData(message,PojoCloudEventDataMapper.from(mapper, TransactionBooked.class));
        var order = deserializedData.getValue();
        var checkoutId = this.paymentOrderRepository.booked(UUID.fromString(order.paymentOrder().getId()));
        this.publisher.publishEvent(new PaymentOrderBookedEvent(this,checkoutId, order.paymentOrder()));
      }else {
        LOGGER.error(String.format(ERROR, message.getId()));
      }
    }
  }

}
