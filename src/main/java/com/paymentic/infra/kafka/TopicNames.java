package com.paymentic.infra.kafka;

import org.apache.kafka.common.protocol.types.Field.Str;

public enum TopicNames {
  PAYMENTS("payments");
  private final String name;

  TopicNames(String name) {
    this.name = name;
  }

  public String topicName(){
    return this.name;
  }

}
