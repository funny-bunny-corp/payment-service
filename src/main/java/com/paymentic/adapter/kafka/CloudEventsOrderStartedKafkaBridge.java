package com.paymentic.adapter.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentic.domain.events.PaymentOrderStartedEvent;
import com.paymentic.domain.repositories.PaymentOrderRepository;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.CloudEventUtils;
import io.cloudevents.core.data.PojoCloudEventData;
import io.cloudevents.jackson.PojoCloudEventDataMapper;
import java.util.UUID;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CloudEventsOrderStartedKafkaBridge {
  private static final String PAYMENT_ORDER_STARTED_EVENT_TYPE = "paymentic.payments.gateway.v1.payment.started";
  private final ObjectMapper mapper;
  private final PaymentOrderRepository paymentOrderRepository;
  public CloudEventsOrderStartedKafkaBridge(ObjectMapper mapper,
      PaymentOrderRepository paymentOrderRepository) {
    this.mapper = mapper;
    this.paymentOrderRepository = paymentOrderRepository;
  }
  @KafkaListener(id = "paymentOrderStarted",groupId = "payment-service-group-id", topics = "payments")
  public void listen(CloudEvent message){
    if (PAYMENT_ORDER_STARTED_EVENT_TYPE.equals(message.getType())){
      PojoCloudEventData<PaymentOrderStartedEvent> deserializedData = CloudEventUtils
          .mapData(message, PojoCloudEventDataMapper.from(mapper, PaymentOrderStartedEvent.class));
      var paymentOrderStarted = deserializedData.getValue();
      this.paymentOrderRepository.markStarted(UUID.fromString(paymentOrderStarted.id()));
    }
  }

}
