package com.vividious.iot.lepetiteprince.event;

import com.vividious.iot.lepetiteprince.model.Action;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;


public record Handshake(@NotNull String sensorName, Set<Action> availableActions,
                        String parentDeviceName, Map<String, String> settings) {

  public Handshake {
    // ensure immutable collection
    if (availableActions == null) {
      availableActions = Set.of();
    } else {
      availableActions = Set.copyOf(availableActions);
    }

    if (settings == null) {
      settings = Map.of();
    } else {
      settings = Map.copyOf(settings);
    }
  }
}
