# MQ Demo (Spring Boot + IBM MQ)

This demo shows a Spring Boot 3.5.x application (Java 21, Maven) that:

- Exposes a `POST /api/messages` endpoint accepting a **plain text** body.
- Sends the received text to an IBM MQ queue.
- Contains an MQ listener that consumes messages from the queue and logs them with SLF4J.
- Activates the listener **only when** the `listener` Spring profile is enabled.
- Includes a `docker-compose.yml` to start a local IBM MQ developer queue manager.

## Prerequisites

- Docker / Docker Desktop
- Java 21
- Maven 3.9+

## Start IBM MQ locally

From the project root:

```bash
docker compose up -d
```

This starts an IBM MQ queue manager `QM1` on port `1414` and creates:

- Queue: `async-hello-world`
- User: `app` with password `passw0rd`, allowed to put/get on that queue.

You can access the MQ web console on https://localhost:9443 (admin user/password are both `passw0rd`
in this demo configuration).

## Run the Spring Boot application

In another terminal:

```bash
mvn spring-boot:run
```

or

```bash
mvn clean package
java -jar target/mq-demo-0.0.1-SNAPSHOT.jar
```

By default the application starts on port `8080`.

## Send a message

Use `curl` or any HTTP client to send a plain text message:

```bash
curl -v -X POST "http://localhost:8080/api/messages"          -H "Content-Type: text/plain"          --data "Hello from Spring Boot to IBM MQ!"
```

The message is sent to the queue `async-hello-world`.

## Enable the MQ listener

The listener is only active when the `listener` profile is enabled.

### Option 1: via environment variable

```bash
SPRING_PROFILES_ACTIVE=listener mvn spring-boot:run
```

### Option 2: via command-line argument

```bash
java -jar target/mq-demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=listener
```

When the listener is active, any message arriving on `async-hello-world` will be logged:

```text
INFO  c.e.m.mq.MqListener - Received message from MQ queue 'async-hello-world': Hello from Spring Boot to IBM MQ!
```

## Configuration

All MQ connection parameters and the queue name are configurable in `src/main/resources/application.yaml`:

```yaml
app:
  mq:
    queue-name: async-hello-world

ibm:
  mq:
    queueManager: QM1
    channel: DEV.APP.SVRCONN
    connName: localhost(1414)
    user: app
    password: passw0rd
```

Adjust these values if your MQ setup differs (for example, different host, port, or queue name).

## Notes

- For a production setup, secure credentials via environment variables or an external secrets store.
- The `mq-jms-spring-boot-starter` dependency configures the JMS `ConnectionFactory` and integrates
  IBM MQ with Spring Boot's `JmsTemplate` and `@JmsListener` infrastructure.
