package com.vividious.iot.lepetiteprince.service;

import com.vividious.iot.lepetiteprince.model.Action;

public interface SensorActionExecutor {

  void triggerActivation(String sensorName);
  void triggerDeactivation(String sensorName);

  void triggerAction(String sensorName, Action action);
}
