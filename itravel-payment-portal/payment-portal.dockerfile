FROM        adoptopenjdk:11-jre-hotspot

WORKDIR     /app

COPY        target/payment-portal.jar .

ENV         PORT 8850
EXPOSE      $PORT:$PORT

ENTRYPOINT  ["java", "-jar", "/app/payment-portal.jar","-Dserver.port=${PORT}"]
