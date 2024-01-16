package com.vividious.iot.lepetiteprince.integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

public class MqttMessageConverter extends AbstractMessageConverter {

  private final ObjectMapper objectMapper;

  public MqttMessageConverter(ObjectMapper objectMapper) {
    super(new MimeType("application", "json"));
    this.objectMapper = objectMapper;
  }

  @Override
  protected boolean supports(@NotNull Class<?> clazz) {
    return (byte[].class.equals(clazz));
  }

  @Override
  @SneakyThrows
  //TODO: handle parsing problems
  protected Object convertToInternal(Object payload, MessageHeaders headers,
      Object conversionHint) {
    return objectMapper.readValue((byte[]) payload, (Class<?>) conversionHint);
  }
}
