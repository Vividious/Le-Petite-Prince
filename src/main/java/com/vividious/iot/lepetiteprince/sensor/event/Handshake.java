package com.vividious.iot.lepetiteprince.sensor.event;

import com.vividious.iot.lepetiteprince.sensor.model.Action;
import jakarta.validation.constraints.NotNull;
import java.util.List;


public record Handshake(@NotNull String name, List<Action> availableActions) {

  public Handshake {
    // ensure immutable collection
    if (availableActions == null) {
      availableActions = List.of();
    } else {
      availableActions = List.copyOf(availableActions);
    }
  }
}
