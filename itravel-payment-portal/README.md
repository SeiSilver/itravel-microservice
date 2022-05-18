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
docker-compose -f docker-compose-dev.yml up
```
5. Run AuthPortalApplication.java first (if docker not found) 
6. Run [APIName]PortalApplication.java to run

## Deployment instructions
### Application Properties

| Key | Required | Default | Description |
| ---- | ---- | ---- | ---- |
| spring.datasource.url | :white_check_mark: | | E.g: jdbc:mysql://localhost:5317/itraveldb |
| spring.datasource.username | :white_check_mark: | | DB username |
| spring.datasource.password | :white_check_mark: | | DB password |
| eureka.client.service-url.defaultZone | :white_check_mark: | http://localhost:8010/eureka | register client to eureka server |
| application.authConfig.tokenSecret | :white_check_mark: | | code to generate token |
| spring.security.oauth2.client.registration.google.client-id | :white_check_mark: | | Google client id |
| spring.security.oauth2.client.registration.google.client-secret | :white_check_mark: | | Google client secret |
| spring.security.oauth2.client.registration.google.scope | :white_check_mark: | profile, email | Google scope |
| application.merchantPortal.url | :white_check_mark: |  | A url address to call api merchant portal |
| application.merchantPortal.secretKey | :white_check_mark: |  | A private key to access merchant portal |

### Environment variables

| Key | Required | API Portal | Description |
| ---- | ---- | ---- | ---- |
| AWS_ACCESS_KEY_ID | Yes | | Access Key ID for AWS |
| AWS_SECRET_ACCESS_KEY | Yes | | Access Key Secret for AWS |
| DATABASE_URL | Yes | ALL (except Eureka Server) | Database URL |
| DATABASE_USERNAME | Yes | ALL (except Eureka Server) | Database username |
| DATABASE_PASSWORD | Yes | ALL (except Eureka Server) | Database password |
| EUREKA_SERVER_URL | Yes | ALL (except Eureka Server) | Eureka server url |

This file should contain lines in the following format:
```shell
[default]
aws_access_key_id = your_access_key_id
aws_secret_access_key = your_secret_access_key
```

### CI/CD with Auto DevOps

This template is compatible with [Auto DevOps](https://docs.gitlab.com/ee/topics/autodevops/).

If Auto DevOps is not already enabled for this project, you can [turn it on](https://docs.gitlab.com/ee/topics/autodevops/#enabling-auto-devops) in the project settings.