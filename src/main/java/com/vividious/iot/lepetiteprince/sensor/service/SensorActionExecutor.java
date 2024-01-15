package com.vividious.iot.lepetiteprince.sensor.service;

import com.vividious.iot.lepetiteprince.sensor.model.Action;
import com.vividious.iot.lepetiteprince.sensor.model.Sensor;

public interface SensorActionExecutor {

  void triggerActivation(String sensorName);
  void triggerDeactivation(String sensorName);

  void triggerAction(String sensorName, Action action);
}
