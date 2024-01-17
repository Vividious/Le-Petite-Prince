package com.vividious.iot.lepetiteprince.integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

public class MqttMessageConverter extends AbstractMessageConverter {

  private final ObjectMapper objectMapper;
  private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(MqttMessageConverter.class);

  public MqttMessageConverter(ObjectMapper objectMapper) {
    super(new MimeType("application", "json"));
    this.objectMapper = objectMapper;
  }

  @Override
  protected boolean supports(@NotNull Class<?> clazz) {
    return (byte[].class.equals(clazz));
  }

  @Override
  protected Object convertToInternal(Object payload, MessageHeaders headers,
      Object conversionHint) {
    try {
      return objectMapper.readValue((byte[]) payload, (Class<?>) conversionHint);
    } catch (IOException e) {
      log.error("Exception when converting MQTT message to internal channel message: {}", e.getMessage());
      return null;
    }
  }
}
