FROM gradle:8.9.0-jdk17 AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY . .
RUN  chmod +x ./gradlew && ./gradlew clean build --refresh-dependencies --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/demo-0.0.1-SNAPSHOT.jar /app/demo-0.0.1-SNAPSHOT.jar
EXPOSE 8080

ENV DATABASE_URL=jdbc:postgresql://db:5432/market_place

CMD ["java", "-jar", "-Dspring.datasource.url=${DATABASE_URL}", "/app/demo-0.0.1-SNAPSHOT.jar"]
