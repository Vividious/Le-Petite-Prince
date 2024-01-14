package com.vividious.iot.lepetiteprince.sensor.infrastructure.integration;

import com.vividious.iot.lepetiteprince.sensor.event.Handshake;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
public class MqttIntegrationService {

  @ServiceActivator(inputChannel = "mqttHandshakeInboundChannel")
  public void handleInboundMessage(Message<Handshake> message){
    System.out.println(message.getHeaders());
    System.out.println(message.getHeaders().get(MessageHeaders.ID));
    System.out.println(message.getHeaders().get(MessageHeaders.CONTENT_TYPE));

    System.out.println(message.getPayload().name());
    System.out.println(message.getPayload().availableActions());
  }
}
