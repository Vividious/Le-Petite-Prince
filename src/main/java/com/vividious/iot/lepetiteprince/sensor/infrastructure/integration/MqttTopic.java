package com.vividious.iot.lepetiteprince.sensor.infrastructure.integration;


import com.vividious.iot.lepetiteprince.sensor.model.Action;
import com.vividious.iot.lepetiteprince.sensor.model.Sensor;


public record MqttTopic(String name) {
  public static final String ROOT_TOPIC = "vividious-iot";
  public static final String SINGLE_LEVEL_WILDCARD = "+";
  public static final String MULTI_LEVEL_WILDCARD = "#";

  public static MqttTopic construct(String base, String... segments) {
    StringBuilder sb = new StringBuilder(base);

    for (var segment : segments) {
      sb.append("/").append(segment);
    }

    return new MqttTopic(sb.toString());
  }


  public static MqttTopic constructActionTopicFor(Sensor sensor, Action action) {
    return MqttTopic.construct(ROOT_TOPIC, sensor.getParentDevice(), "actions", action.name());
  }

  public static MqttTopic constructHandshakeResponseTopic(Sensor sensor){
    return MqttTopic.construct(ROOT_TOPIC, "good_to_see_you", sensor.getName());
  }

  public static MqttTopic constructHandshakeTopic(){
    return MqttTopic.construct(ROOT_TOPIC, SINGLE_LEVEL_WILDCARD, "howdy");
  }
}
