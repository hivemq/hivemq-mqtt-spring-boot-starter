package com.hivemq.client.spring.autoconfigure;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.hivemq.HiveMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = HiveMQMqttAutoConfiguration.class)
@ContextConfiguration(initializers = HiveMQMqttAutoConfigurationTest.Initializer.class)
@Testcontainers
class HiveMQMqttAutoConfigurationTest {

    @Container
    static final HiveMQContainer hivemq = new HiveMQContainer(DockerImageName.parse("hivemq/hivemq-ce"));

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            Map<String, Object> properties = Map.of(
                    "hivemq.client.server-uri", "tcp://" + hivemq.getHost() + ":" +hivemq.getMqttPort()
            );

            configurableApplicationContext
                    .getEnvironment()
                    .getPropertySources()
                    .addFirst(new MapPropertySource("testProperties", properties));
        }
    }

    @Autowired
    private Mqtt5AsyncClient mqtt5AsyncClient;

    @Test
    void shouldCreateHiveMQMqttClient() {
        assertTrue(mqtt5AsyncClient.getState().isConnected());
    }
}