package com.vividious.iot.lepetiteprince.service;

import static com.vividious.iot.lepetiteprince.integration.MqttTopic.ROOT_TOPIC;
import static com.vividious.iot.lepetiteprince.integration.MqttTopic.constructActionTopicFor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividious.iot.lepetiteprince.event.ActionDetails;
import com.vividious.iot.lepetiteprince.integration.MqttTopic;
import com.vividious.iot.lepetiteprince.integration.service.MqttIntegrationGateway;
import com.vividious.iot.lepetiteprince.model.Sensor;
import com.vividious.iot.lepetiteprince.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class ActionExecutor {

  private final MqttIntegrationGateway mqttIntegrationGateway;
  private final SensorRepository sensorRepository;
  private final ObjectMapper objectMapper;

  public void executeAction(ActionDetails actionDetails) {
    Sensor sensor = sensorRepository.findByName(actionDetails.getSensorName());

    if (sensor == null || !sensor.isSupported(actionDetails.getAction())) {
      return;
    }

    MqttTopic actionTopic = constructActionTopicFor(sensor, actionDetails.getAction());

    sendActionDetailsOnMqtt(actionDetails, actionTopic);
  }

  public void triggerHandshakes(){
    MqttTopic mqttTopic = MqttTopic.construct(ROOT_TOPIC, "sensors_assemble");

    mqttIntegrationGateway.send(mqttTopic.name(), "Sensors Assemble!", 1);
  }

  private void sendActionDetailsOnMqtt(ActionDetails actionDetails, MqttTopic actionTopic) {
    try {
      mqttIntegrationGateway.send(actionTopic.name(),
          objectMapper.writeValueAsString(actionDetails),
          1);
    } catch (JsonProcessingException e) {
      log.error("Exception when serializing actionDetails object: {}", e.getMessage());
    }
  }
}
