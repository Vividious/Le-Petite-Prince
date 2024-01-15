package com.vividious.iot.lepetiteprince.sensor.infrastructure.integration.config;

import org.eclipse.paho.mqttv5.client.IMqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.ClientManager;
import org.springframework.integration.mqtt.core.Mqttv5ClientManager;

@Configuration
public class MqttClientConfiguration {

  @Bean
  public ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager(
      @Value("${mqtt.uri}") String brokerUri,
      @Value("${mqtt.connection-timeout:30000}") int connectionTimeout,
      @Value("${mqtt.max-reconnect-delay:1000}") int maxReconnectDelay,
      @Value("${mqtt.auth.username}") String username,
      @Value("${mqtt.auth.password}") byte[] password,
      @Value("${mqtt.client-id}") String clientId
  ) {
    MqttConnectionOptions connectionOptions = new MqttConnectionOptions();
    connectionOptions.setServerURIs(new String[]{brokerUri});
    connectionOptions.setConnectionTimeout(connectionTimeout);
    connectionOptions.setMaxReconnectDelay(maxReconnectDelay);
    connectionOptions.setAutomaticReconnect(true);
    connectionOptions.setUserName(username);
    connectionOptions.setPassword(password);
    Mqttv5ClientManager clientManager = new Mqttv5ClientManager(connectionOptions, clientId);
    clientManager.setPersistence(new MqttDefaultFilePersistence());
    return clientManager;
  }
}
