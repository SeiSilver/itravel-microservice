FROM        adoptopenjdk:11-jre-hotspot

WORKDIR     /app

ENV         PORT 8043

COPY        target/shop-portal.jar .

EXPOSE      $PORT:$PORT

ENTRYPOINT  ["java", "-jar", "/app/shop-portal.jar","-Dserver.port=${PORT}"]
