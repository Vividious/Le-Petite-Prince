package com.vividious.iot.lepetiteprince.infrastructure;

import org.eclipse.paho.mqttv5.client.IMqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.ClientManager;
import org.springframework.integration.mqtt.core.Mqttv5ClientManager;
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MqttConfiguration {

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
  @Bean
  public MessageChannel testChannel() {
    return new DirectChannel();
  }

  @Bean
  public IntegrationFlow mqttInFlowTestTopic(
      ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager,
      @Qualifier("testChannel") MessageChannel testChannel) {

    Mqttv5PahoMessageDrivenChannelAdapter messageProducer =
        new Mqttv5PahoMessageDrivenChannelAdapter(clientManager, "test");
    return IntegrationFlow.from(messageProducer)
        .channel(testChannel)
        .get();
  }

  @ServiceActivator(inputChannel = "testChannel")
  public void handleMessage(Message<String> message) {
    System.out.println("Received Message: " + message.getPayload());
  }
}
