package com.paymentic.adapter.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentic.domain.events.PaymentOrderStartedEvent;
import com.paymentic.domain.events.WalletUpdatedEvent;
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
public class CloudEventsWalletUpdated {
  private static final String TRANSACTION_REGISTERED_EVENT_TYPE = "paymentic.payments-gateway.v1.transaction-registered";
  private static final Logger LOGGER = LoggerFactory.getLogger(CloudEventsWalletUpdated.class);
  private static final String ERROR = "Event %s already handled!!!";
  private final ObjectMapper mapper;
  private final PaymentOrderRepository paymentOrderRepository;
  private final EventService eventService;

  public CloudEventsWalletUpdated(ObjectMapper mapper,
      PaymentOrderRepository paymentOrderRepository, EventService eventService) {
    this.mapper = mapper;
    this.paymentOrderRepository = paymentOrderRepository;
    this.eventService = eventService;
  }
  @KafkaListener(id = "paymentOrderWalletUpdated",groupId = "payment-service-group-id", topics = "payments")
  public void listen(CloudEvent message){
    var handle = this.eventService.shouldHandle(new Event(UUID.fromString(message.getId())));
    if (handle){
      if (TRANSACTION_REGISTERED_EVENT_TYPE.equals(message.getType())){
        PojoCloudEventData<WalletUpdatedEvent> deserializedData = CloudEventUtils
            .mapData(message, PojoCloudEventDataMapper.from(mapper, WalletUpdatedEvent.class));
        var walletUpdatedEvent = deserializedData.getValue();
        this.paymentOrderRepository.walletUpdated(UUID.fromString(walletUpdatedEvent.payment().getId()));
      }
    }else {
      LOGGER.error(String.format(ERROR, message.getId()));
    }
  }

}
