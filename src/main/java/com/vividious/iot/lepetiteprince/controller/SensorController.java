package com.vividious.iot.lepetiteprince.controller;

import com.vividious.iot.lepetiteprince.model.Sensor;
import com.vividious.iot.lepetiteprince.repository.SensorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SensorController {

  private final SensorRepository sensorRepository;

  @GetMapping("/sensors")
  public List<Sensor> getAll() {
    return sensorRepository.findAll();
  }
}
