package com.vividious.iot.lepetiteprince.sensor.event;

import com.vividious.iot.lepetiteprince.sensor.model.Action;
import jakarta.validation.constraints.NotNull;
import java.util.Set;


public record Handshake(@NotNull String sensorName, Set<Action> availableActions, String parentDeviceName) {

  public Handshake {
    // ensure immutable collection
    if (availableActions == null) {
      availableActions = Set.of();
    } else {
      availableActions = Set.copyOf(availableActions);
    }
  }
}
