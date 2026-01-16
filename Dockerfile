FROM ubuntu:24.04

RUN apt-get update
RUN apt-get install -y eclipse-temurin:21
COPY . .

RUN apt-get install -y maven
RUN mvn clean install

FROM eclipse-temurin:21-jdk-slim

EXPOSE 8080

COPY --from=build /target/desafio-rits-tecnologia-backend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]