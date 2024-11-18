# Replicated Log

> A fault-tolerant distributed system for log replication with tunable write concerns and eventual consistency.

![Project Status](https://img.shields.io/badge/status-completed-green.svg)

## 📋 Description
**Replicated Log** is a distributed system designed to ensure reliable log replication between a Master server and multiple Secondary servers. It supports:
- Blocking and tunable semi-synchronous replication.
- Flexible write concern levels to control replication guarantees.
- Fault tolerance via retries, deduplication, and total ordering of messages.
- Easy deployment with Dockerized architecture.

### Key Features:
1. Blocking replication with acknowledgements (ACK).
2. Write concern levels (`w`) to define the number of ACKs required before responding to clients.
3. Exactly-once message delivery with deduplication.
4. Total ordering of messages across all nodes.
5. Health monitoring using a heartbeat mechanism.
6. Quorum-based writes with read-only fallback in case of insufficient active nodes.

## 🛠 Tech Stack
- **Java 11**
- **Spring Boot**
- **Maven**
- **Docker**
- **Lombok**

## 🚀 How to Run
To deploy the project using Docker:

1. Set the active profile to `docker` in the `application.properties` file of secondary servers:
   - For `secondary-server-1`:
     ```properties
     spring.profiles.active=docker
     ```
   - For `secondary-server-2`:
     ```properties
     spring.profiles.active=docker
     ```

2. Build the project:
   ```bash
   mvn clean package
This will generate the following JAR files:

- **target/replicated-log-master.jar**
- **target/replicated-log-secondary-1.jar**
- **target/replicated-log-secondary-2.jar**

3. Run the Docker Compose configuration:
   ```bash
   docker-compose up


## Functional Iterations

### Iteration 0
- Implemented a simple Echo Client-Server application to establish a foundation.

### Iteration 1

![image](https://github.com/user-attachments/assets/90c40fe4-1d10-40c4-8d60-f471b69ce4db)

- Deployment architecture includes:
  - **Master**:
    - `POST /`: Append messages to an in-memory list.
    - `GET /`: Retrieve all messages from the in-memory list.
  - **Secondary**:
    - `GET /`: Retrieve all replicated messages.
- Key properties:
  - Master replicates messages to all secondaries and waits for ACKs.
  - Blocking replication: POST completes only after all ACKs are received.
  - Logging enabled.
- Tested with Dockerized Master and Secondary servers.

### Iteration 2

![image](https://github.com/user-attachments/assets/9eb9c6a6-279f-4398-8ec1-9d297d377478)


- Added **Write Concern** for semi-synchronous replication:
  - Clients specify `w` (write concern level) in POST requests.
  - `w=1`: Acknowledgement from Master only.
  - `w=2`: Acknowledgements from Master and one Secondary.
  - `w=n`: Acknowledgements from Master and `n-1` Secondaries.
- Introduced artificial delays to emulate replicas inconsistency and eventual consistency.
- Enhanced system with:
  - **Message deduplication**.
  - **Total order guarantee** for messages across all nodes.

### Iteration 3

![image](https://github.com/user-attachments/assets/fca68d9a-e330-4c5a-bf1e-7655a892de91)


- Added advanced features:
  - **Retry Mechanism**:
    - Retries for message delivery to ensure "exactly-once" semantics.
    - Support for unavailable secondary nodes with delayed replication.
    - Parallel clients operate without mutual blocking.
  - **Deduplication**:
    - Avoid duplicate messages in secondary logs.
  - **Total Order**:
    - Messages appear in the same order on all nodes.
- Acceptance Test:
  1. Start Master and Secondary 1.
  2. Send `POST (Msg1, w=1)`: Success.
  3. Send `POST (Msg2, w=2)`: Success.
  4. Send `POST (Msg3, w=3)`: Wait (until Secondary 2 starts).
  5. Send `POST (Msg4, w=1)`: Success.
  6. Start Secondary 2.
  7. Verify messages: `[Msg1, Msg2, Msg3, Msg4]`.

### Additional Features
- **Heartbeats**:
  - Periodic health checks for secondaries.
  - Health statuses: Healthy → Suspected → Unhealthy.
  - API: `GET /health` to query secondary statuses.
- **Quorum Append**:
  - Master enters read-only mode if quorum is lost.
