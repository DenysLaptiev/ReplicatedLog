# ReplicatedLog

## Running in Docker

1. Open `application.properties` file of `secondary-server-1` module and set active profile to docker

```
spring.profiles.active=docker
``` 

2. Open `application.properties` file of `secondary-server-2` module and set active profile to docker

```
spring.profiles.active=docker
``` 

3. Open terminal at project root directory and execute

```bash
mvn clean package
``` 
After that corresponding jar-files in each module should appear:

```
target/replicated-log-master.jar
```
```
target/replicated-log-secondary-1.jar
```
```
target/replicated-log-secondary-2.jar
```
4. Run docker-compose.yml file.