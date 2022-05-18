FROM        adoptopenjdk:11-jre-hotspot

WORKDIR     /app

COPY        target/eureka-server.jar .

ENV         PORT 8010
ENV         EUREKA_SERVER_URL http://eureka-server:8010/eureka/
ENV         EUREKA_HOSTNAME eureka-server
EXPOSE      $PORT:$PORT

ENTRYPOINT  ["java", "-jar", "/app/eureka-server.jar","-Dserver.port=${PORT}"]
