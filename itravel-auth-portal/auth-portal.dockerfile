FROM        adoptopenjdk:11-jre-hotspot

WORKDIR     /app

COPY        target/auth-portal.jar .

ENV         PORT 8011
EXPOSE      $PORT:$PORT

ENTRYPOINT  ["java", "-jar", "/app/auth-portal.jar","-Dserver.port=${PORT}"]
