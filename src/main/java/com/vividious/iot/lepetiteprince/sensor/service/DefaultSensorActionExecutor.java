package com.vividious.iot.lepetiteprince.sensor.service;

import static com.vividious.iot.lepetiteprince.sensor.infrastructure.integration.MqttTopic.constructActionTopicFor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividious.iot.lepetiteprince.sensor.event.ActionDetails;
import com.vividious.iot.lepetiteprince.sensor.infrastructure.integration.service.MqttIntegrationGateway;
import com.vividious.iot.lepetiteprince.sensor.infrastructure.integration.MqttTopic;
import com.vividious.iot.lepetiteprince.sensor.model.Action;
import com.vividious.iot.lepetiteprince.sensor.model.Sensor;
import com.vividious.iot.lepetiteprince.sensor.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultSensorActionExecutor implements SensorActionExecutor {

  private final MqttIntegrationGateway mqttIntegrationGateway;
  private final SensorRepository sensorRepository;
  private final ObjectMapper objectMapper;

  //TODO: proper exception handling

  @Override
  @SneakyThrows
  public void triggerActivation(String sensorName) {
    Sensor sensor = sensorRepository.findByName(sensorName);

    if (sensor == null || !sensor.canActivate()) {
      return;
    }

    MqttTopic actionTopic =  constructActionTopicFor(sensor, Action.TURN_ON);
    ActionDetails actionDetails = new ActionDetails(sensorName);

    mqttIntegrationGateway.send(actionTopic.name(), objectMapper.writeValueAsString(actionDetails) , 1);
  }

  @Override
  @SneakyThrows
  public void triggerDeactivation(String sensorName) {
    Sensor sensor = sensorRepository.findByName(sensorName);

    if (sensor == null || !sensor.canDeactivate()) {
      return;
    }

    MqttTopic actionTopic =  constructActionTopicFor(sensor, Action.TURN_OFF);
    ActionDetails actionDetails = new ActionDetails(sensorName);

    mqttIntegrationGateway.send(actionTopic.name() , objectMapper.writeValueAsString(actionDetails), 1);
  }

  @Override
  public void triggerAction(String sensorName, Action action) {
    //TODO: replace with proper design pattern
    if(action.equals(Action.TURN_ON)){
      triggerActivation(sensorName);
    }else if(action.equals(Action.TURN_OFF)){
      triggerDeactivation(sensorName);
    }
  }

}
