FROM gradle:8.9.0-jdk17 AS builder
WORKDIR /app
COPY build.gradle /app
COPY settings.gradle /app
COPY . /app
#RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon
RUN chmod +x ./gradlew && ./gradlew build --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/demo-0.0.1-SNAPSHOT.jar /app/demo-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/demo-0.0.1-SNAPSHOT.jar"]
