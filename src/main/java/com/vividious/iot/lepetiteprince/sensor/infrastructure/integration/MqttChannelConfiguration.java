package com.vividious.iot.lepetiteprince.sensor.infrastructure.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividious.iot.lepetiteprince.sensor.event.Handshake;
import org.eclipse.paho.mqttv5.client.IMqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.ClientManager;
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.Mqttv5PahoMessageHandler;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MqttChannelConfiguration {

  public static final String SENSOR_HANDSHAKE_TOPIC = "raspberry_home/+/howdy";

  @Bean
  public MessageChannel mqttHandshakeInboundChannel() {
    return new QueueChannel();
  }

  @Bean
  public MqttMessageConverter mqttMessageConverter(ObjectMapper objectMapper){
    return new MqttMessageConverter(objectMapper);
  }

  @Bean
  public MessageProducer handshakeMessageProducer(
      ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager, MqttMessageConverter mqttMessageConverter) {
    Mqttv5PahoMessageDrivenChannelAdapter messageProducer =
        new Mqttv5PahoMessageDrivenChannelAdapter(clientManager, SENSOR_HANDSHAKE_TOPIC);

    messageProducer.setQos(1);
    messageProducer.setOutputChannel(mqttHandshakeInboundChannel());
    messageProducer.setPayloadType(Handshake.class);
    messageProducer.setMessageConverter(mqttMessageConverter);

    return messageProducer;
  }

  @Bean
  public MessageChannel mqttOutboundChannel() {
    return new QueueChannel();
  }

  @Bean
  public IntegrationFlow mqttOutFlow(
      ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager,
      @Qualifier("mqttOutboundChannel") MessageChannel mqttOutboundChannel) {
    Mqttv5PahoMessageHandler mqttv5PahoMessageHandler = new Mqttv5PahoMessageHandler(
        clientManager);

    return IntegrationFlow.from(mqttOutboundChannel)
        .handle(mqttv5PahoMessageHandler)
        .get();
  }

}
