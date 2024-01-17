package com.vividious.iot.lepetiteprince.event;

import com.vividious.iot.lepetiteprince.model.Action;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import lombok.Value;

@Value
public class ActionDetails {

  @NotBlank String sensorName;

  @NotNull
  Action action;

  Map<String, String> settings = new HashMap<>();
}
