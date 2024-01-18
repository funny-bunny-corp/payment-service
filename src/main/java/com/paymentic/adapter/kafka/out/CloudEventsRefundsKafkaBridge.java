package com.paymentic.adapter.kafka.out;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentic.domain.events.RefundCreatedEvent;
import com.paymentic.domain.events.publisher.RefundEventsPublisher;
import com.paymentic.infra.ce.CExtensions;
import com.paymentic.infra.ce.CExtensions.Audience;
import com.paymentic.infra.ce.CExtensions.EventContext;
import com.paymentic.infra.kafka.TopicNames;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.data.PojoCloudEventData;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CloudEventsRefundsKafkaBridge implements RefundEventsPublisher {
  private final KafkaTemplate<String, CloudEvent> sender;
  private final ObjectMapper mapper;
  private final Logger logger = LoggerFactory.getLogger(CloudEventsRefundsKafkaBridge.class);
  private final Timer refundCreatedTimer;
  public CloudEventsRefundsKafkaBridge(KafkaTemplate<String, CloudEvent> sender, ObjectMapper mapper,
      MeterRegistry meterRegistry) {
    this.sender = sender;
    this.mapper = mapper;
    this.refundCreatedTimer = meterRegistry.timer("refund_created_message","broker","kafka");
  }

  @Override
  public void refundCreated(RefundCreatedEvent event) {
    var ce = CloudEventBuilder.v1()
        .withId(UUID.randomUUID().toString())
        .withSource(URI.create(event.source()))
        .withSubject(event.subject())
        .withType(event.type())
        .withData(PojoCloudEventData.wrap(event, mapper::writeValueAsBytes))
        .withExtension(CExtensions.AUDIENCE.extensionName(), Audience.EXTERNAL_BOUNDED_CONTEXT.audienceName())
        .withExtension(CExtensions.EVENT_CONTEXT.extensionName(), EventContext.DOMAIN.eventContextName())
        .build();
    Supplier<CompletableFuture<Void>> messageSupplier = () -> this.sender.send(TopicNames.PAYMENTS.topicName(), ce)
        .thenRun(() -> logger.info("Message sent. Id: {}; Data: {}", ce.getId(), event));
    this.refundCreatedTimer.record(messageSupplier);
  }
}
