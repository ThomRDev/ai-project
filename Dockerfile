FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs

WORKDIR /app/src/main/resources/frontend
RUN npm install
RUN npm run build
RUN rm -rf node_modules

WORKDIR /app
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

RUN apt-get update && \
    apt-get install -y ffmpeg curl unzip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/*.jar app.jar

COPY setup-vosk.sh /app/setup-vosk.sh
RUN chmod +x /app/setup-vosk.sh
RUN /app/setup-vosk.sh

ENV VOSK_MODEL_PATH=/app/models/vosk-model-small-es-0.42
ENV TRANSCRIBER_ENGINE=vosk
ENV APP_PORT=8080

ENTRYPOINT ["java", "-jar", "app.jar"]
