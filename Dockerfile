FROM maven:3.8.1-jdk-11 AS builder
COPY ./ /usr/src/mymaven
WORKDIR /usr/src/mymaven
RUN mvn clean package

FROM openjdk:11.0.10-jdk
MAINTAINER Mário Sousa <super.mario.santos.sousa@gmail.com>
COPY --from=builder /usr/src/mymaven/target/*.jar /app.jar 
EXPOSE 5000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]