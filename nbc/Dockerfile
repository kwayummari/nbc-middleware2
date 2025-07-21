FROM ubuntu:latest
LABEL authors="kelvin.chingoma"
MAINTAINER itrust.co.tz

FROM eclipse-temurin:21
WORKDIR /app
COPY target/nbc-middleware-1.0.0.jar /app
EXPOSE 10103
ENTRYPOINT ["java","-jar","/app/nbc-middleware-1.0.0.jar"]
