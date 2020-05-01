FROM maven:3.3-jdk-8 AS builder
COPY ./ /usr/src/mymaven
WORKDIR /usr/src/mymaven
RUN mvn clean package

FROM openjdk:8-jdk-alpine
MAINTAINER Mário Sousa <super.mario.santos.sousa@gmail.com>
COPY --from=builder /usr/src/mymaven/target/*.jar /app.jar 
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]