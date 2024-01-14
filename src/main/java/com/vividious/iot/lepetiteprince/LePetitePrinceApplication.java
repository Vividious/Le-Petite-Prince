package com.vividious.iot.lepetiteprince;

import com.vividious.iot.lepetiteprince.sensor.infrastructure.integration.MqttIntegrationGateway;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LePetitePrinceApplication {

  public static void main(String[] args) {
    SpringApplication.run(LePetitePrinceApplication.class, args);
  }

  @Bean
  public ApplicationRunner mqttMessagePublishTest(MqttIntegrationGateway gate) {
    return args ->
        gate.send("raspberry_home/esp32_1/good_to_see_you",
            "That's one small step for man, one giant leap for mankind", 2);
  }

}
