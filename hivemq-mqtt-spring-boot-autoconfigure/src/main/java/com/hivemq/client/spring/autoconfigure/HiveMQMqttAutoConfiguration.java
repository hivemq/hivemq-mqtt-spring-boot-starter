/*
 * Copyright (c) 2024-present HiveMQ and the HiveMQ Community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expres or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.hivemq.client.spring.autoconfigure;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.auth.Mqtt5EnhancedAuthMechanism;
import com.hivemq.client.spring.config.MqttProperties;
import com.hivemq.client.spring.converter.PasswordConverter;
import com.hivemq.client.spring.factories.Mqtt3ClientFactory;
import com.hivemq.client.spring.factories.Mqtt5ClientFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

/**
 * Auto configuration for HiveMQ MQTT client.
 * @since 1.0
 * @author Sven Kobow
 */
@Configuration
@EnableConfigurationProperties(MqttProperties.class)
@ComponentScan(basePackages = { "com.hivemq.client.spring" })
public class HiveMQMqttAutoConfiguration {

    private final MqttProperties mqttProperties;

    public HiveMQMqttAutoConfiguration(final MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
    }

    @Bean
    @ConditionalOnProperty(name = "hivemq.client.mqtt-version", havingValue = "5", matchIfMissing = true)
    public Mqtt5ClientFactory mqtt5ClientFactory() {
        return new Mqtt5ClientFactory();
    }

    @Bean(destroyMethod = "disconnect")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "hivemq.client.mqtt-version", havingValue = "5", matchIfMissing = true)
    public Mqtt5AsyncClient mqtt5AsyncClient(final Mqtt5ClientFactory clientFactory, @Nullable Mqtt5EnhancedAuthMechanism enhancedAuthMechanism) {
        if (mqttProperties.getMqttVersion() == 3) {
            throw new BeanCreationException("Mqtt5AsyncClient is not available for MQTT version 3. Use Mqtt3AsyncClient instead.");
        }

        return clientFactory.mqttClient(mqttProperties, enhancedAuthMechanism);
    }

    @Bean
    @ConditionalOnProperty(name = "hivemq.client.mqtt-version", havingValue = "3")
    public Mqtt3ClientFactory mqtt3ClientFactory() {
        return new Mqtt3ClientFactory();
    }

    @Bean(destroyMethod = "disconnect")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "hivemq.client.mqtt-version", havingValue = "3")
    public Mqtt3AsyncClient mqtt3AsyncClient(final Mqtt3ClientFactory clientFactory) {
        if (mqttProperties.getMqttVersion() == 5) {
            throw new BeanCreationException("Mqtt3AsyncClient is not available for MQTT version 5. Use Mqtt5AsyncClient instead.");
        }

        return clientFactory.mqttClient(mqttProperties);
    }
}
