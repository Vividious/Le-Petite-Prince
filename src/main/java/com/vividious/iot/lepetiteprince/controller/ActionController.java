package com.vividious.iot.lepetiteprince.controller;

import com.vividious.iot.lepetiteprince.model.Action;
import com.vividious.iot.lepetiteprince.repository.SensorRepository;
import com.vividious.iot.lepetiteprince.service.SensorActionExecutor;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ActionController {

  private final SensorRepository sensorRepository;

  private final SensorActionExecutor actionExecutor;

  @GetMapping("/actions/{sensorName}")
  public Set<Action> allAvailableActionsFor(@PathVariable("sensorName") String sensorName) {
    return sensorRepository.findByName(sensorName).getActions();
  }

  @PostMapping("/actions/{sensorName}/{action}")
  public void triggerActionFor(@PathVariable("sensorName") String sensorName,
      @PathVariable("action") Action action) {

    actionExecutor.triggerAction(sensorName, action);
  }
}
