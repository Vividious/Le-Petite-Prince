package com.vividious.iot.lepetiteprince.controller;

import com.vividious.iot.lepetiteprince.event.ActionDetails;
import com.vividious.iot.lepetiteprince.model.Action;
import com.vividious.iot.lepetiteprince.repository.SensorRepository;
import com.vividious.iot.lepetiteprince.service.ActionExecutor;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ActionController {

  private final SensorRepository sensorRepository;

  private final ActionExecutor actionExecutor;

  @GetMapping("/actions/{sensorName}")
  public Set<Action> allAvailableActionsFor(@PathVariable("sensorName") String sensorName) {
    return sensorRepository.findSupportedActionsByName(sensorName);
  }

  @PostMapping("/actions")
  public void triggerActionFor(@Valid @RequestBody ActionDetails actionDetails) {
    actionExecutor.executeAction(actionDetails);
  }
}
