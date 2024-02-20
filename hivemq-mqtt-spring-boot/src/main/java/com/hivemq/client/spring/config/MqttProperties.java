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

package com.hivemq.client.spring.config;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttClientTransportConfig;
import com.hivemq.client.mqtt.lifecycle.MqttClientAutoReconnect;
import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3Connect;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5ConnectRestrictions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

import java.io.File;
import java.net.URI;
import java.time.Duration;
import java.util.Map;

/**
 * Configuration properties for HiveMQ MQTT client.
 * @since 1.0.0
 * @author Sven Kobow
 */
@ConfigurationProperties(prefix = "hivemq.client")
public class MqttProperties {
    private URI serverUri = URI.create(String.format("tcp://%s:%s", MqttClient.DEFAULT_SERVER_HOST, MqttClient.DEFAULT_SERVER_PORT));
    private String clientId = null;
    private int mqttVersion = 5;
    private Duration connectionTimeout = Duration.ofSeconds(MqttClientTransportConfig.DEFAULT_MQTT_CONNECT_TIMEOUT_MS);
    private Boolean manualAcks = false;
    private byte[] password = null;
    private String userName = null;
    private Integer keepAliveInterval = Mqtt5Connect.DEFAULT_KEEP_ALIVE;
    private Long maxReconnectDelay = MqttClientAutoReconnect.DEFAULT_MAX_DELAY_S;
    private boolean automaticReconnect = false;
    private Map<String, String> customWebSocketHeaders = null;
    private WillMessageProperties willMessageProperties = null;
    private SslProperties sslProperties = null;
    private boolean cleanSession = Mqtt3Connect.DEFAULT_CLEAN_SESSION;
    private boolean cleanStart = Mqtt5Connect.DEFAULT_CLEAN_START;
    private Long sessionExpiryInterval = Mqtt5Connect.DEFAULT_SESSION_EXPIRY_INTERVAL;
    private Integer receiveMaximum = Mqtt5ConnectRestrictions.DEFAULT_RECEIVE_MAXIMUM;
    private Integer maximumPacketSize = Mqtt5ConnectRestrictions.DEFAULT_MAXIMUM_PACKET_SIZE;
    private Integer topicAliasMaximum = Mqtt5ConnectRestrictions.DEFAULT_TOPIC_ALIAS_MAXIMUM;
    private boolean requestResponseInfo = Mqtt5ConnectRestrictions.DEFAULT_REQUEST_RESPONSE_INFORMATION;
    private boolean requestProblemInfo = Mqtt5ConnectRestrictions.DEFAULT_REQUEST_PROBLEM_INFORMATION;
    private Map<String, String> userProperties;

    public boolean isCleanStart() {
        return cleanStart;
    }

    /**
     * Set true if a new sessions should be started for connection (v5 only).
     * @param cleanStart if connection should start a new session.
     */
    public void setCleanStart(boolean cleanStart) {
        this.cleanStart = cleanStart;
    }

    public Long getSessionExpiryInterval() {
        return sessionExpiryInterval;
    }

    /**
     * The session expiry interval in seconds (v5 only).
     * @param sessionExpiryInterval the session expiry interval in seconds.
     */
    public void setSessionExpiryInterval(Long sessionExpiryInterval) {
        this.sessionExpiryInterval = sessionExpiryInterval;
    }

    public Integer getReceiveMaximum() {
        return receiveMaximum;
    }

    /**
     * The maximum amount of not acknowledged publishes with QoS 1 or 2 the client accepts from the server concurrently (v5 only).
     * @param receiveMaximum the maximum amount of not acknowledged publishes with QoS 1 or 2 the client accepts from the server concurrently.
     */
    public void setReceiveMaximum(Integer receiveMaximum) {
        this.receiveMaximum = receiveMaximum;
    }

    public Integer getMaximumPacketSize() {
        return maximumPacketSize;
    }

    /**
     * The maximum packet size the client sends to the server (v5 only).
     * @param maximumPacketSize the maximum packet size the client sends to the server.
     */
    public void setMaximumPacketSize(Integer maximumPacketSize) {
        this.maximumPacketSize = maximumPacketSize;
    }

    public Integer getTopicAliasMaximum() {
        return topicAliasMaximum;
    }

    /**
     * The maximum amount of topic aliases the client accepts from the server (v5 only).
     * @param topicAliasMaximum the maximum amount of topic aliases the client accepts from the server.
     */
    public void setTopicAliasMaximum(Integer topicAliasMaximum) {
        this.topicAliasMaximum = topicAliasMaximum;
    }

    public boolean isRequestResponseInfo() {
        return requestResponseInfo;
    }

    /**
     * Whether the client requests response information from the server (v5 only).
     * @param requestResponseInfo whether the client requests response information from the server.
     */
    public void setRequestResponseInfo(boolean requestResponseInfo) {
        this.requestResponseInfo = requestResponseInfo;
    }

    public boolean isRequestProblemInfo() {
        return requestProblemInfo;
    }

    /**
     * Whether the client requests problem information from the server (v5 only).
     * @param requestProblemInfo whether the client requests problem information from the server.
     */
    public void setRequestProblemInfo(boolean requestProblemInfo) {
        this.requestProblemInfo = requestProblemInfo;
    }

    public Map<String, String> getUserProperties() {
        return userProperties;
    }

    /**
     * The user defined properties that should be sent for every message (v5 only).
     * @param userProperties the user defined properties tha should be sent for every message.
     */
    public void setUserProperties(Map<String, String> userProperties) {
        this.userProperties = userProperties;
    }

    /**
     * Set true if a new session should be started for connection (v3 only).
     * @param cleanSession True if a new session should be started for connection.
     */
    public void setCleanSession(final boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public boolean isCleanSession() {
        return this.cleanSession;
    }

    /**
     * The URI of server to connect to as [schema]://[serverHost]:[serverPort].
     * @return the uri of server to connect to as [schema]://[serverHost]:[serverPort].
     */
    public URI getServerUri() {
        return this.serverUri;
    }

    /**
     * @param serverUri the uri of server to connect to as [schema]://[serverHost]:[serverPort].
     */
    public void setServerUri(@Nullable final URI serverUri) {
        this.serverUri = serverUri;
    }

    /**
     * @return the server host of the configured {@link #serverUri}.
     */
    public String getServerHost() {
        return serverUri != null ? serverUri.getHost() : null;
    }

    /**
     * @return the server port of the configured {@link #serverUri}.
     */
    public Integer getServerPort() {
        return serverUri != null ? serverUri.getPort() : null;
    }

    /**
     * @return true if the schema of the configured {@link #serverUri} is ssl.
     */
    public boolean isSSL() {
        return serverUri != null && "SSL".equalsIgnoreCase(serverUri.getScheme());
    }

    /**
     * The client identifier to use.
     * @return the client identifier.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId the client identifier.
     */
    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    /**
     * The version of the MQTT protocol to use (one of 3 or 5).
     * @return the MQTT version to use.
     */
    public int getMqttVersion() {
        return mqttVersion;
    }

    /**
     * @param mqttVersion the MQTT version to use. Use 3 for MQTT version 3.1.1 or 5 for MQTT version 5. Default is MQTT version 5.
     */
    public void setMqttVersion(int mqttVersion) {
        this.mqttVersion = mqttVersion;
    }

    /**
     * How long to wait for a connection to be established.
     * @return the connection timeout.
     */
    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * @param connectionTimeout how long to wait for a connection.
     */
    public void setConnectionTimeout(final Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * True if you wish to manually acknowledge messages.
     * @return an optional boolean to set the client in manual acknowledge mode.
     */
    public boolean getManualAcks() {
        return this.manualAcks;
    }

    /**
     * @param manualAcks set to true if you wish to manually acknowledge messages.
     */
    public void setManualAcks(final boolean manualAcks) {
        this.manualAcks = manualAcks;
    }

    /**
     * The password to use for MQTT connections.
     * @return the password to use for MQTT connections.
     */
    public byte[] getPassword() {
        return password;
    }

    /**
     * @param password the password to use for MQTT connections.
     */
    public void setPassword(@Nullable final byte[] password) {
        this.password = password;
    }

    /**
     * The username to use for MQTT connections.
     * @return the username to use for MQTT connections.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the username to use for MQTT connections.
     */
    public void setUserName(@Nullable final String userName) {
        this.userName = userName;
    }

    /**
     * The maximum delay for reconnecting in seconds.
     * @return the maximal delay for reconnecting.
     */
    public Long getMaxReconnectDelay() {
        return maxReconnectDelay;
    }

    /**
     * @param maxReconnectDelay the maximum delay for reconnecting.
     */
    public void setMaxReconnectDelay(final long maxReconnectDelay) {
        this.maxReconnectDelay = maxReconnectDelay;
    }

    /**
     * The keep alive interval in seconds.
     * @return the keep alive interval.
     */
    public Integer getKeepAliveInterval() {
        return keepAliveInterval;
    }

    /**
     * @param keepAliveInterval the keep alive interval.
     */
    public void setKeepAliveInterval(final int keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    /**
     * True if the client should attempt to reconnect to the server if the connection is lost.
     * @return true is automatic reconnect should be performed.
     */
    public boolean isAutomaticReconnect() {
        return automaticReconnect;
    }

    /**
     * @param automaticReconnect if an automatic reconnect should be performed.
     */
    public void setAutomaticReconnect(final boolean automaticReconnect) {
        this.automaticReconnect = automaticReconnect;
    }

    /**
     * Custom headers that should be sent with web socket connections.
     * @return the custom headers that should be sent with web socket connections.
     */
    public Map<String, String> getCustomWebSocketHeaders() {
        return customWebSocketHeaders;
    }

    /**
     * @param customWebSocketHeaders the custom headers that should be sent with web socket connections.
     */
    public void setCustomWebSocketHeaders(@Nullable final Map<String, String> customWebSocketHeaders) {
        this.customWebSocketHeaders = customWebSocketHeaders;
    }

    /**
     * @return the last will message that should be sent on ungraceful disconnects.
     */
    public WillMessageProperties getWillMessage() {
        return willMessageProperties;
    }

    /**
     * @param willMessageProperties the last will message that should be sent on ungraceful disconnects.
     */
    public void setWillMessage(@Nullable final WillMessageProperties willMessageProperties) {
        this.willMessageProperties = willMessageProperties;
    }

    /**
     * @return the ssl properties to use for SSL and mTLS.
     */
    public SslProperties getSslProperties() {
        return sslProperties;
    }

    /**
     * @param sslProperties the ssl properties to use for SSL and mTLS.
     */
    public void setCertificateConfiguration(@Nullable final SslProperties sslProperties) {
        this.sslProperties = sslProperties;
    }

    public static class WillMessageProperties {

        private String topic;
        private byte[] payload;
        private int qos;
        private boolean retained;

        /**
         * @return The topic to publish to
         */
        public String getTopic() {
            return topic;
        }

        /**
         * @param topic The topic to publish to
         */
        public void setTopic(String topic) {
            this.topic = topic;
        }

        /**
         * @return The message payload
         */
        public byte[] getPayload() {
            return payload;
        }

        /**
         * @param payload The message payload
         */
        public void setPayload(byte[] payload) {
            this.payload = payload;
        }

        /**
         * @return The message qos
         */
        public int getQos() {
            return qos;
        }

        /**
         * @param qos The message qos
         */
        public void setQos(int qos) {
            this.qos = qos;
        }

        /**
         * @return True if the message should be retained
         */
        public boolean isRetained() {
            return retained;
        }

        /**
         * @param retained If the message should be retained
         */
        public void setRetained(boolean retained) {
            this.retained = retained;
        }
    }

    public static class SslProperties {

        private File certificateAuthority;
        private File certificate;
        private File privateKey;
        private char[] password;

        /**
         * @return The certificate authority
         */
        public File getCertificateAuthority() {
            return certificateAuthority;
        }

        /**
         * @param certificateAuthority The certificate authority location
         */
        public void setCertificateAuthority(File certificateAuthority) {
            this.certificateAuthority = certificateAuthority;
        }

        /**
         * @return The certificate
         */
        public File getCertificate() {
            return certificate;
        }

        /**
         * @param certificate The client certificate location
         */
        public void setCertificate(File certificate) {
            this.certificate = certificate;
        }

        /**
         * @return The client key
         */
        public File getPrivateKey() {
            return privateKey;
        }

        /**
         * @param privateKey The client private key location
         */
        public void setPrivateKey(File privateKey) {
            this.privateKey = privateKey;
        }

        /**
         * @return The key password
         */
        public char[] getPassword() {
            return password;
        }

        /**
         * @param password The key password
         */
        public void setPassword(char[] password) {
            this.password = password;
        }
    }
}
