FROM docker.io/library/maven:3.9.11-eclipse-temurin-21-alpine

WORKDIR /
COPY target/ckb-explorer-web.jar app.jar

CMD ["java", "-jar", "-Dproject.name=ckb-explorer-web", "/app.jar"]
