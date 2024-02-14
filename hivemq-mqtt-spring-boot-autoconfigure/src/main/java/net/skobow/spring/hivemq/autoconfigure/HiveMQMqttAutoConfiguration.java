package net.skobow.spring.hivemq.autoconfigure;

import com.hivemq.client.mqtt.MqttClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HiveMQMqttAutoConfiguration {

    @Bean
    public MqttClient mqttClient() {
        return MqttClient.builder()
                .useMqttVersion5()
                .serverHost("localhost")
                .serverPort(1883)
                .build();
    }
}
