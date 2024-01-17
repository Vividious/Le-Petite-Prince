package com.vividious.iot.lepetiteprince.model;

import static com.vividious.iot.lepetiteprince.model.SensorActionLog.Status.FAILED;
import static com.vividious.iot.lepetiteprince.model.SensorActionLog.Status.SUCCESS;

import com.vividious.iot.lepetiteprince.event.ActionDetails;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SensorActionLog {

  enum Status {SUCCESS, FAILED}

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private UUID sensorId;

  private String sensorName;

  private String sensorParentDevice;

  @Enumerated(EnumType.STRING)
  private Action action;

  @Enumerated(EnumType.STRING)
  private Status actionExecutionStatus;

  @CreationTimestamp
  private LocalDateTime created;

  public static SensorActionLog createSuccessfulExecutionLog(Sensor sensor, ActionDetails actionDetails) {

    return SensorActionLog.builder()
        .sensorId(sensor.getId())
        .sensorName(actionDetails.getSensorName())
        .sensorParentDevice(sensor.getParentDevice())
        .actionExecutionStatus(SUCCESS)
        .action(actionDetails.getAction())
        .build();
  }

  public static SensorActionLog createFailedExecutionLog(ActionDetails actionDetails) {
    return SensorActionLog.builder()
        .actionExecutionStatus(FAILED)
        .sensorName(actionDetails.getSensorName())
        .action(actionDetails.getAction())
        .build();
  }
}
