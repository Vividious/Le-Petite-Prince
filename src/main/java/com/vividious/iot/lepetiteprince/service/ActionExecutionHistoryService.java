package com.vividious.iot.lepetiteprince.service;

import com.vividious.iot.lepetiteprince.event.ActionDetails;
import com.vividious.iot.lepetiteprince.model.Sensor;
import com.vividious.iot.lepetiteprince.model.SensorActionLog;
import com.vividious.iot.lepetiteprince.repository.SensorActionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionExecutionHistoryService {

  private final SensorActionLogRepository sensorActionLogRepository;

  public void createSensorActionLog(Sensor sensor, ActionDetails actionDetails) {
    SensorActionLog actionLog =
        sensor != null ? SensorActionLog.createSuccessfulExecutionLog(sensor, actionDetails)
            : SensorActionLog.createFailedExecutionLog(actionDetails);

    sensorActionLogRepository.save(actionLog);
  }
}
