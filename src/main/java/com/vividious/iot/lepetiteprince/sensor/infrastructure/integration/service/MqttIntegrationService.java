package com.vividious.iot.lepetiteprince.sensor.infrastructure.integration.service;

import static com.vividious.iot.lepetiteprince.sensor.infrastructure.integration.MqttTopic.constructHandshakeResponseTopic;

import com.vividious.iot.lepetiteprince.sensor.event.Handshake;
import com.vividious.iot.lepetiteprince.sensor.infrastructure.integration.MqttTopic;
import com.vividious.iot.lepetiteprince.sensor.model.Sensor;
import com.vividious.iot.lepetiteprince.sensor.service.SensorRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MqttIntegrationService {

  private final MqttIntegrationGateway mqttIntegrationGateway;

  private final SensorRegistrar sensorRegistrar;

  @ServiceActivator(inputChannel = "mqttHandshakeInboundChannel")
  public void handleInboundMessage(Message<Handshake> message) {
    Handshake handshake = message.getPayload();

    Sensor sensor = sensorRegistrar.registerWith(handshake);

    MqttTopic handshakeResponseTopic = constructHandshakeResponseTopic(sensor);

    mqttIntegrationGateway.send(handshakeResponseTopic.name(), "Hello " + sensor.getName(), 1,
        true);
  }

}
