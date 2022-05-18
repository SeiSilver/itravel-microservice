FROM        adoptopenjdk:11-jre-hotspot

WORKDIR     /app

COPY        target/account-portal.jar .

ENV         PORT 8020
EXPOSE      $PORT:$PORT

ENTRYPOINT  ["java", "-jar", "/app/account-portal.jar","-Dserver.port=${PORT}"]
