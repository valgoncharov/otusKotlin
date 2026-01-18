FROM alpine:3.19

# Установка OpenJDK 17 JRE
RUN apk update && \
    apk add --no-cache openjdk17-jre

WORKDIR /app

COPY build/libs/OtusHomeWork-1.0-SNAPSHOT-all.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]