FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar analytics-service.jar

ENTRYPOINT ["java", "-jar", "analytics-service.jar"]
