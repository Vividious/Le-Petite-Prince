package com.vividious.iot.lepetiteprince.sensor.infrastructure.integration;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttIntegrationGateway {

  void send(@Header(MqttHeaders.TOPIC) String topic, String message, @Header(MqttHeaders.QOS) int qos);

}
