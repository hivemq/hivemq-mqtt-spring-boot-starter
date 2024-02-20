# HiveMQ MQTT Spring Boot Starter

[![CI Check](https://github.com/hivemq/hivemq-mqtt-spring-boot-starter/actions/workflows/check.yml/badge.svg)](https://github.com/hivemq/hivemq-mqtt-spring-boot-starter/actions/workflows/check.yml)

This Spring Boot Starter can be used to add and configure the [HiveMQ MQTT Client](https://github.com/hivemq/hivemq-mqtt-client) to your Spring project.

__IMPORTANT: This is still work in progress and not yet ready for production use!__

## How to use

### Add the dependency

The starter is not yet available on Maven Central. In order to use it in your project you would need to build and publish it locally: Building and publishing locally is straight forward. Just clone the repository and run the following command:

```shell
./gradlew publishToMavenLocal
````

Check `gradle.properties` for the current version.

#### Gradle
```kotlin
// add local maven repository
repositories {
    mavenLocal()
}

// add the dependency
implementation("com.hivemq:hivemq-mqtt-spring-boot-starter:<CURRENT_VERSION>")


```

#### Maven
```xml
<!-- add local maven repository -->
<repositories>
    <repository>
        <id>local-maven-repo</id>
        <url>file://${user.home}/.m2/repository</url>
    </repository>
</repositories>

<!-- add the dependency -->
<dependency>
    <groupId>com.hivemq</groupId>
    <artifactId>hivemq-mqtt-spring-boot-starter</artifactId>
    <version><CURRENT_VERSION></version>
</dependency>
```

### Autowire the client

The HiveMQ MQTT Client supports 3 different [API flavors](https://hivemq.github.io/hivemq-mqtt-client/docs/api-flavours/): 
- Blocking 
- Async 
- Reactive. 

It is possible to autowire the client for preferred API. Simple declare a dependency of the desired type in your Spring component.

#### Blocking Client
```java
@Service
public class MyService {

    private final Mqtt5BlockingClient mqttClient;

    public MyService(final Mqtt5BlockingClient mqttClient) {
        this.mqttClient = mqttClient;
    }
}
```

#### Async Client
```java
@Service
public class MyService {

    private final Mqtt5AsyncClient mqttClient;

    public MyService(final Mqtt5AsyncClient mqttClient) {
        this.mqttClient = mqttClient;
    }
}
```

#### Reactive Client
```java
@Service
public class MyService {

    private final Mqtt5RxClient mqttClient;

    public MyService(final Mqtt5RxClient mqttClient) {
        this.mqttClient = mqttClient;
    }
}
```

See the official [HiveMQ MQTT Client Documentation](https://hivemq.github.io/hivemq-mqtt-client/) for more details on how to use the client. 

## Configuration

| Property                  | Description | Default |
|---------------------------|-------------|---------|
| `hivemq.client.server-uri` | The URI of the MQTT broker to connect to. | `tcp://localhost:1883` |
| `hivemq.client.client-id` | The client id to use for the connection. | `null` |
| `hivemq.client.mqtt-version` | The MQTT version to use. | `5` |
| `hivemq.client.connection-timeout` | The connection timeout in milliseconds. | `10000` |
| `hivemq.client.manual-acks` | Whether to use manual acks. | `false` |
| `hivemq.client.password` | The password to use for the connection. | `null` |
| `hivemq.client.username` | The username to use for the connection. | `null` |
| `hivemq.client.keep-alive-interval` | The keep alive interval in seconds. | `60` |
| `hivemq.client.max-reconnect-delay` | The maximum reconnect delay in milliseconds. | `30000` |
| `hivemq.client.automatic-reconnect` | Whether to automatically reconnect. | `false` |
| `hivemq.client.will-message.payload` | The payload of the will message. | `null` |
| `hivemq.client.will-message.qos` | The QoS of the will message. | `0` |
| `hivemq.client.will-message.topic` | The topic of the will message. | `null` |
| `hivemq.client.will-message.retained` | Whether the will message should be retained. | `false` |
| `hivemq.client.ssl-properties.certificate` | The path to the client certificate. | `null` |
| `hivemq.client.ssl-properties.private-key` | The path to the client private key. | `null` |
| `hivemq.client.ssl-properties.certificate-authority` | The path to the certificate authority. | `null` |
| `hivemq.client.ssl-properties.password` | The password for the client certificate. | `null` |
| `hivemq.client.clean-session` | Whether to use a clean session. | `false` |
| `hivemq.client.clean-start` | Whether to use a clean start. | `false` |
| `hivemq.client.session-expiry-interval` | The session expiry interval in seconds. | `10000` |
| `hivemq.client.receive-maximum` | The maximum number of incoming messages. | `100` |
| `hivemq.client.maximum-packet-size` | The maximum packet size. | `10000` |
| `hivemq.client.topic-alias-maximum` | The maximum number of topic aliases. | `10` |
| `hivemq.client.request-problem-info` | Whether to request problem info. | `false` |
| `hivemq.client.request-response-info` | Whether to request response info. | `false` |
| `hivemq.client.user-properties.key1` | The key of the first user property. | `null` |


```properties
hivemq.client.server-uri=tcp://localhost:1883
hivemq.client.client-id=client1
hivemq.client.mqtt-version=5
hivemq.client.connection-timeout=10000
hivemq.client.manual-acks=false
hivemq.client.password=123
hivemq.client.username=user
hivemq.client.keep-alive-interval=60
hivemq.client.max-reconnect-delay=30000
hivemq.client.automatic-reconnect=false
hivemq.client.will-message.payload=offline
hivemq.client.will-message.qos=1
hivemq.client.will-message.topic=will
hivemq.client.will-message.retained=false
hivemq.client.ssl-properties.certificate=<PATH-TO>/client.crt
hivemq.client.ssl-properties.private-key=<PATH-TO>/client.key
hivemq.client.ssl-properties.certificate-authority=<PATH-TO>/ca.crt
hivemq.client.ssl-properties.password=123
hivemq.client.clean-session=false
hivemq.client.clean-start=false
hivemq.client.session-expiry-interval=10000
hivemq.client.receive-maximum=100
hivemq.client.maximum-packet-size=10000
hivemq.client.topic-alias-maximum=10
hivemq.client.request-problem-info=false
hivemq.client.request-response-info=false
hivemq.client.user-properties.key1=value1
```