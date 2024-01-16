package com.vividious.iot.lepetiteprince.service;

import com.vividious.iot.lepetiteprince.event.Handshake;
import com.vividious.iot.lepetiteprince.model.Sensor;
import com.vividious.iot.lepetiteprince.repository.SensorRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SensorRegistrar {

  private final SensorRepository sensorRepository;

  public Sensor registerWith(Handshake handshake) {
    UUID existingId = sensorRepository.findIdByName(handshake.sensorName());

    Sensor sensor = Sensor.createFrom(handshake, existingId);

    return sensorRepository.save(sensor);
  }


}
