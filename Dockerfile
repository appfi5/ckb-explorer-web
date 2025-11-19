FROM docker.io/library/maven:3.9.11-eclipse-temurin-21-alpine

WORKDIR /
COPY target/ckb-explorer-web.jar app.jar
COPY target/newrelic/newrelic.jar newrelic.jar
COPY newrelic/newrelic.yml newrelic.yml

CMD ["java","-javaagent:/newrelic.jar", "-jar", "-Dproject.name=ckb-explorer-web", "/app.jar"]
