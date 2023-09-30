FROM maven:3.8.3-openjdk-17 as maven-builder
LABEL authors="santuli"

WORKDIR /tmp

COPY . ./

RUN mvn clean package -DskipTests=true

FROM openjdk:17-jdk-slim-buster

ENV JAR_FILE=target/*.jar

COPY --from=maven-builder /tmp/$JAR_FILE /opt/app/
RUN mv /opt/app/*.jar /opt/app/app.jar

WORKDIR /opt/app

RUN chgrp -R 0 /opt/app && \
    chmod -R g=u /opt/app

ENV PORT 8080

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","app.jar"]