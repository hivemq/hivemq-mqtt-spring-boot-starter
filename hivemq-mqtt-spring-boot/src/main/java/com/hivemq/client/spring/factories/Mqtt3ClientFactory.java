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

package com.hivemq.client.spring.factories;

import com.hivemq.client.mqtt.*;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.lifecycle.MqttClientAutoReconnect;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3ClientBuilder;
import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3Connect;
import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3ConnectBuilder;
import com.hivemq.client.spring.config.MqttProperties;
import com.hivemq.client.spring.exception.MqttClientException;
import com.hivemq.client.spring.ssl.KeyManagerFactoryCreationException;
import com.hivemq.client.spring.ssl.TrustManagerFactoryCreationException;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * A factory to create an MQTT v3 client.
 *
 * @author Sven Kobow
 * @since 1.0.0
 */
public final class Mqtt3ClientFactory implements MqttClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(Mqtt3ClientFactory.class);

    public Mqtt3AsyncClient mqttClient(final MqttProperties configuration) {

        final Mqtt3ClientBuilder clientBuilder = MqttClient.builder()
            .useMqttVersion3()
            .identifier(configuration.getClientId())
            .transportConfig(buildTransportConfig(configuration));

        if (configuration.isAutomaticReconnect()) {
            clientBuilder.automaticReconnect()
                .initialDelay(MqttClientAutoReconnect.DEFAULT_START_DELAY_S, TimeUnit.SECONDS)
                .maxDelay(configuration.getMaxReconnectDelay(), TimeUnit.SECONDS)
                .applyAutomaticReconnect();
        }

        final Mqtt3ConnectBuilder connectBuilder = Mqtt3Connect.builder()
            .cleanSession(configuration.isCleanSession())
            .keepAlive(configuration.getKeepAliveInterval());

        if (StringUtils.isNotEmpty(configuration.getUserName())) {
            connectBuilder.simpleAuth()
                .username(configuration.getUserName())
                .password(configuration.getPassword())
                .applySimpleAuth();
        }

        if (configuration.getWillMessage() != null) {
            var willMessage = configuration.getWillMessage();

            connectBuilder.willPublish()
                .topic(willMessage.getTopic())
                .payload(willMessage.getPayload())
                .qos(Objects.requireNonNull(MqttQos.fromCode(willMessage.getQos())))
                .retain(willMessage.isRetained());
        }

        final var client = clientBuilder.buildAsync();

        if (LOG.isTraceEnabled()) {
            LOG.trace("Connecting to {} on port {}", configuration.getServerHost(), configuration.getServerPort());
        }

        client.connect(connectBuilder.build())
            .whenComplete((mqtt3ConnAck, throwable) -> {
                if (throwable != null) {
                    throw new MqttClientException("Error connecting mqtt client");
                }
            })
            .join();

        return client;
    }

    private MqttClientTransportConfig buildTransportConfig(final MqttProperties configuration) {

        final MqttClientTransportConfigBuilder transportConfigBuilder = MqttClientTransportConfig.builder()
            .serverHost(configuration.getServerHost())
            .serverPort(configuration.getServerPort())
            .mqttConnectTimeout(configuration.getConnectionTimeout().toMillis(), TimeUnit.MILLISECONDS);

        if (configuration.isSSL() && configuration.getSslProperties() != null) {
            final MqttProperties.SslProperties sslConfiguration = configuration.getSslProperties();
            final MqttClientSslConfigBuilder sslConfigBuilder = MqttClientSslConfig.builder();
//            if (configuration.isHttpsHostnameVerificationEnabled()) {
//                sslConfigBuilder.hostnameVerifier(configuration.getSSLHostnameVerifier());
//            }

            try {
                sslConfigBuilder.trustManagerFactory(getTrustManagerFactory(sslConfiguration));

                if (sslConfiguration.getCertificate() != null) {
                    sslConfigBuilder.keyManagerFactory(getKeyManagerFactory(sslConfiguration));
                }

            } catch (KeyManagerFactoryCreationException | TrustManagerFactoryCreationException e) {
                throw new BeanInstantiationException(MqttClientTransportConfig.class, "Error creating SSL configuration", e);
            }

//            if (configuration.isHttpsHostnameVerificationEnabled()) {
//                sslConfigBuilder.hostnameVerifier(configuration.getSSLHostnameVerifier());
//            }
        }

        return transportConfigBuilder.build();
    }
}
