# Getting Started

## Setup instructions for Developers
1. [Install Docker & Docker Compose](https://docs.docker.com/get-docker/)
2. [Install Maven](https://mkyong.com/maven/how-to-install-maven-in-windows/)
3. Run Maven
```shell
mvn clean install -DskipTests
```
4. Run following command
```shell
cd .deploy
docker-compose -f docker-compose.yml up
```
5. Run EurekaServerApplication.java

## Deployment instructions
### Application Properties

| Key | Required | Default | Description |
| ---- | ---- | ---- | ---- |
| eureka.client.service-url.defaultZone | Yes | http://localhost:8010/eureka/ |  |
| eureka.client.registerWithEureka | Yes | false |  |
| eureka.client.fetchRegistry | Yes | false |  |
| eureka.instance.hostname | Yes | localhost | |
| server.port | Yes | 8010 | |
### Environment variables

| Key | Required | API Portal | Description |
| ---- | ---- | ---- | ---- |
| EUREKA_HOSTNAME | Yes | ALL (except Eureka Server) | Database password |
| EUREKA_SERVER_URL | Yes | ALL (except Eureka Server) | Eureka server url |